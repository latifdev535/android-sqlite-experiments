
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

See the comments in `ThreadsSingleConnectionDeadlock.java` for a more detailed description.

## Related blog articles

* [Android Sqlite Locking](http://touchlabblog.tumblr.com/post/24474398246/android-sqlite-locking)
* [Single SQLite connection](http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection) 

