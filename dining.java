//This is your driver class.
class Driver {

  public static void main(String [ ] args) {
    
    Semaphore chop1 = new Semaphore(1);
    Semaphore chop2 = new Semaphore(1);
    Semaphore chop3 = new Semaphore(1);
    Semaphore chop4 = new Semaphore(1);
    Semaphore chop5 = new Semaphore(1);
    
    Semaphore waiter = new Semaphore(4);
    
    Philosopher p1 = new Philosopher(1,chop1,chop2,waiter); 
    Philosopher p2 = new Philosopher(2,chop2,chop3,waiter);
    Philosopher p3 = new Philosopher(3,chop3,chop4,waiter);
    Philosopher p4 = new Philosopher(4,chop4,chop5,waiter);
    Philosopher p5 = new Philosopher(5,chop5,chop1,waiter);
    
    p1.setPriority(5);
    p2.setPriority(5);
    p3.setPriority(5);
    p4.setPriority(5);
    p5.setPriority(5);
    
    p1.start();
    p2.start();
    p3.start();
    p4.start();
    p5.start();
    
  }
}
class Philosopher extends Thread {

  Semaphore LEFT_CHOPSTICK, RIGHT_CHOPSTICK, WAITER ;
  int philosopher_id ;

    public Philosopher(int i, Semaphore LEFT_CHOPSTICK, Semaphore RIGHT_CHOPSTICK, Semaphore WAITER) {
    this.philosopher_id = i ;
    this.LEFT_CHOPSTICK = LEFT_CHOPSTICK ;
    this.RIGHT_CHOPSTICK = RIGHT_CHOPSTICK ;
    this.WAITER = WAITER ;
  }

  private int random(int n) {
    return (int) Math.round( n * Math.random() - 0.5 ) ;
  }

//  This method calls the do_something methods with "EATING" or "THINKING" 
//    as an argument to indicate what the philosopher will be doing for 
// a random amount of time.

  private void do_something(String s) { 
    System.out.println("PHILOSOPHER " + philosopher_id + " is " + s) ;
    try{ sleep(random(10)) ; } catch (InterruptedException e) { }
  }

  public void run() {
    for(int i=0;i<20;i++){ 
        //Your pre-entry protocol should go here.
        //This philosopher should secure her left chopstick
        //and right chopstick before she is allowed to proceed.
        
        WAITER.down();
        LEFT_CHOPSTICK.down();
        RIGHT_CHOPSTICK.down();

        do_something("EATING");

        //Your post-exit protocol should go here.
        //This philosopher should release her left chopstick
        //and right chopstick, perhaps notifying her neighbors
        //before she begins THINKING
        
        LEFT_CHOPSTICK.up();
        RIGHT_CHOPSTICK.up();
        WAITER.up();

        do_something("THINKING");
       }
  }

}

// Class Semaphore implements a counting semaphore.
// It acts as a mutex if the value field is initialized to 1.
// Notice the wait() and notify() function calls. (i.e., sleep and wakeup)

class Semaphore {

  protected int value = 0 ;

  protected Semaphore() { value = 0 ; }

  protected Semaphore(int initial) { value = initial ; }

  public synchronized void down() {
    value-- ;
    if (value < 0)
      try { wait() ; } catch(  InterruptedException e ) { }
  }

  public synchronized void up() {
    value++ ; if (value <= 0) notify() ;
  }
}
