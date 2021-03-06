### 说明
该项目主要是在线程池创建时，对线程池进行增强
将线程池强引用到bootstrap-load-client-0.1.jar中的ThreadPoolMonitorData里
**使用此包的前提是认为线程池是认为线程池的生命周期是跟随项目，启动后不会手动销毁，
如果动态创建线程池并销毁，需要调用shutdown或者shutdowNow，这块会对强引用进行解除
否则因为这块强引用导致资源无法释放，没法被gc回收，如果有动态创建的线程池，需要将线程池的引用改成弱引用**
#### bootstrap-load-client 
此模块需要打成jar包
- 引入项目中；
- 配置bootstrap ClassLoader加载

#### monitor-agent 
此模块需要打成jar包，当一个agent使用
启动参数配置
```
-javaagent:/Users/yxk/git/agent/monitor-agent/target/monitor-agent-0.1.jar=127.0.0.1,8888 -Xbootclasspath/a:/Users/yxk/git/agent/bootstrap-load-client/target/bootstrap-load-client-0.1.jar
```
说明：
- monitor-agent-0.1.jar 为agent包
- bootstrap-load-client-0.1.jar  为Bootstrap ClassLoader加载的包，在增强ThreadPoolExecutor的时候，必须是启动类加载



ps:
如果改造LinkedBlockingDeque队列，请注意
一旦队列满了put会阻塞，如果不再重新添加任务不会触发

```
    public void putFirst(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            //一旦满了阻塞
            while (!linkFirst(node))
                notFull.await();
        } finally {
            lock.unlock();
        }
    }
    public void putLast(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();
        Node<E> node = new Node<E>(e);
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            //一旦满了阻塞
            while (!linkLast(node))
                notFull.await();
        } finally {
            lock.unlock();
        }
    }
    //修改capacity，后后续的线程没问题，但是之前阻塞的不会继续执行，所以加notFull.signalAll();
    public void setCapacity(int capacity) {
      final int oldCapacity = this.capacity;
      this.capacity = capacity;
      final int size = count.get();
      if (capacity > size && size >= oldCapacity) {
          notFull.signalAll();
      }
  }
```
