# DistributedLockJ

![Java](https://img.shields.io/badge/Java-25+-blue?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/build-Maven-red)
![License](https://img.shields.io/badge/License-GPLv3-red.svg)

WORK IN PROGRESS - NOT YET RELEASED.

## How it works

- You can have multiple named locks
- You try to acquire a distributed lock
- A call is made to the distributed lock database to see if the lock can be acquired
  - If the lock can be acquired a lock is placed with the requested name and stored to database
  - If not, the library returns an `DistributedLockInUseException`
- After you're done with the lock, you must release it so that other's can use it

## To Do

- [ ] Add support for removing old entries during startup
  - Use a special 'maintenance' dlock for that which locks all entries for the process using the lock
- [ ] Add support for Azure Storage Account
- [ ] Add support for Azure Cosmos DB
- [ ] Plan how to provide configuration
  - Environment variables
  - Should settings file be supported?
- [ ] Investigate if transactions are needed
  - Azure Table Storage (not needed)
  - Azure Cosmos DB?
