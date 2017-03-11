CREATE TABLE `vote` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(20)  NOT NULL,
    `candidate_id` varchar(10)  NOT NULL,
    `timestamp` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_vote_user_candidate_time` (`user_id`, `candidate_id`, `timestamp`)
);

CREATE INDEX ix_vote_candidate ON `vote` (`candidate_id`);
CREATE INDEX ix_vote_timestamp ON `vote` (`timestamp`);

CREATE TABLE `user_vote_count` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(20)  NOT NULL,
    `vote_count` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_user_vote_count_user` (`user_id`)
);

CREATE INDEX ix_user_vote_count_user ON `user_vote_count` (`user_id`);

CREATE TABLE `accountable_vote` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(20)  NOT NULL,
    `candidate_id` varchar(10)  NOT NULL,
    `timestamp` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UQ_acc_vote_user_candidate_time` (`user_id`, `candidate_id`, `timestamp`)
);

CREATE INDEX ix_accountable_vote_candidate ON `accountable_vote` (`candidate_id`);
CREATE INDEX ix_accountable_vote_timestamp ON `accountable_vote` (`timestamp`);