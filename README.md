# Count Me Up

Web application that allows users to vote for their favourite candidate(s).

### Prerequisites

* Java8
* Maven
* MySQL server
* JMeter (if you wish to run the performance tests)


## Running the tests

```
mvn clean test
```

## Running the application

```
mvn spring-boot:run
```

## Endpoints

Vote for a candidate:

```
POST http://localhost:8080/votes/{candidateId}?userId={userId}
```

Count accountable (valid) votes for all candidates:

```
GET http://localhost:8080/votes/count
```

This returns a JSON response of this form:

```
{
    "voteCounts": [
        {
            "candidateId": "c1",
            "count": 347
        },
        {
            "candidateId": "c2",
            "count": 382
        },
        {
            "candidateId": "c3",
            "count": 353
        },
        {
            "candidateId": "c4",
            "count": 363
        },
        {
            "candidateId": "c5",
            "count": 320
        }
    ]
}

```
## Working solution

This solution uses a MySQL database with three tables:
* `vote` - has all the votes from all users, stored as `user_id`, `candidate_id`, `timestamp`
* `user_vote_count` - has the total number of votes per user
* `accountable_vote` - has the accountable (valid) votes from all users. Its structure is the same as `vote`.

These three tables are transactionally updated when a user casts a vote:
* `vote` -  always append a new vote to this table
* `user_vote_count` - always update the number of votes a user has casted so far. Updates are done using optimistic
locking, using a `version` column.
* `accountable_vote` - append a new vote to this table only if the user has casted less than 3 votes so far.

In addition, we store in memory:
* the timestamp of the last countMeUp run
* the results of the last countMeUp run

The results are updated every time we run countMeUp, using cached value and whatever we find in `accountable_vote`
between the last time we've run countMeUp and the current time.
This solution will run in under one second, because the only work done is a `count` with `group by` on one table for rows
stored in the past second or so.
The following graph shows the average response times for countMeUp, when run every second. The starting point is
when there had already been 1 million votes, and the server is being loaded with about 50 concurrent users voting every
second (see `CountMeUpPerformanceTest.jmx` for the test plan).

![Response times for countMeUp](https://octodex.github.com/images/yaktocat.png)

## Approach
I started with a simple in-memory counter, which kept a map from candidates to their accountable vote counts and an
additional helper map from users to number of casted votes. You can see this solution in `InMemoryVoteCounter.java`
This, of course, was never going to be a viable solution because it didn't persist anything (so the server could go down at any point
and lose all data), and also the cache could not be shared between multiple instances of the same server.
However, it did serve as a good starting point because it made me think of the database structure I've outlined above, with
the helper `user_vote_count` table.

The second solution, using the database, was good but not performant. When running the performance test for just 1 million
votes, it took 30 seconds to run countMeUp. This made me think I needed to use a cache as well, to speed it up.

The cache + database solution was my final iteration on the problem, and it is correct because:
* the vote counts are accurate: they are based on what we store in the database and the current timestamp
* the vote counts are valid: we only store accountable votes in `accountable_vote`
* the cache could be a minor problem if the server went down and a new instance was spawned. However, there are ways to get
 around it, for example by populating it on startup.
* the cache will differ from one server instance to another, but regardless, the counts will be correct, because they are
based on previous values stored in the cache and the database, which is consistent.
