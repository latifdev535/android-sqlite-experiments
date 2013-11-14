
A few experiments (ok, only one right now) investigating the behavior of SQLite on Android.

## Experiments

### ThreadsSingleConnectionDeadlock

* Create a single SQLiteDatabase object that is shared among all threads
* WriterThread spawned
* ReaderThread spawned
* WriterThread opens a transaction and inserts some data
* WriterThread calls .join() on ReaderThread to wait for it to finish
* ReaderThread attempts to read some data
* Deadlock!

Error message in logcat:

```
W/SQLiteConnectionPool( 1055): The connection pool for database '/...testdb' has been unable to grant a connection to thread 79 (Thread-79) with flags 0x1 for 30.001001 seconds.
W/SQLiteConnectionPool( 1055): Connections: 0 active, 1 idle, 0 available.
```

See the comments in `ThreadsSingleConnectionDeadlock.java` for a more detailed description.

## How to run this code

* Import project into Android Studio
* Hit "Run" button

## Related blog articles

* [Android Sqlite Locking](http://touchlabblog.tumblr.com/post/24474398246/android-sqlite-locking)
* [Single SQLite connection](http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection) 

