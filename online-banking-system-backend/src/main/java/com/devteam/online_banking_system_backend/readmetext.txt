. Financial Precision: Implemented BigDecimal arithmetic and HALF_EVEN rounding to ensure 100% accuracy in financial transactions, eliminating floating-point errors.

. Auditability: Engineered a robust TransactionLog system to maintain an immutable audit trail of all account activities, including pre/post-transaction states.

. Automated Services: Designed a Spring-scheduled background task to calculate and apply daily interest rates across thousands of accounts with optimized filtering logic.

. Quality Assurance: Achieved high reliability through comprehensive Integration Testing, simulating complex deposit/overdraft scenarios.

. Concurrency Management: Resolved potential race conditions by implementing Pessimistic Locking (PESSIMISTIC_WRITE) to synchronize simultaneous account access, ensuring data consistency during high-concurrency events.

. Fault-Tolerant Batch Processing: Leveraged Transaction Propagation (REQUIRES_NEW) to isolate individual account updates within bulk processes, preventing system-wide rollbacks and enhancing overall application resilience.

. What was the hardest part of this project? "The hardest part was ensuring that a midnight interest calculation wouldn't conflict with a user's real-time withdrawal. I solved this by implementing a locking strategy and using custom transaction propagation to ensure that even if one account failed, the rest of the bank's records remained accurate and committed."