import random
vote_count = 10000000
user_count = 2000000
candidate_count = 5
with open('votes', 'w') as f:
    for i in range (0, vote_count):
        user = random.randint(1, user_count)
        candidate = random.randint(1, candidate_count)
        f.write("u" + str(user) + "," + "c" + str(candidate) + "\n")
