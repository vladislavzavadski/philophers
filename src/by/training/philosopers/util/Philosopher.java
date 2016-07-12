package by.training.philosopers.util;

import by.training.philosopers.domain.Fork;
import by.training.philosopers.domain.Request;
import by.training.philosopers.domain.RequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by vladislav on 11.07.16.
 */
public class Philosopher implements Runnable {
    private static final Logger logger = LogManager.getRootLogger();
    private volatile boolean isCanEat = false;
    private volatile boolean isRunning = true;
    private int philosopherId;
    private Map<Integer, Fork> forkMap;
    private int leftForkId;
    private int rightForkId;
    private Waiter waiter;

    public Philosopher(int philosopherId, Map<Integer, Fork> forkMap, Waiter waiter){
        this.philosopherId = philosopherId;
        this.forkMap = forkMap;
        this.waiter = waiter;
        leftForkId = philosopherId==1?5:(philosopherId-1);
        rightForkId = philosopherId;

    }

    @Override
    public void run() {
        while (isRunning){
            try {
                think();//философ думает
                wantEat();//философ захотел поесть
            } catch (InterruptedException e) {
                logger.debug("Произошла и ошибка и философ умер.");
                return;

            }

        }
    }

    private void wantEat() throws InterruptedException {
        waiter.sendRequest(new Request(RequestType.WANT_TO_EAT, this));//обращаемся к официанту с просьбой перекусить
        logger.debug("Философ "+philosopherId+" захотел поесть");
        waitFreePlace(); //ожидаем реакции от официанта
    }

    private void waitFreePlace() throws InterruptedException {
        while (!isCanEat){ //ожидание разрешения поесть
            if(!isRunning){
                return; //если главный поток приказал завершится, то завершаем жизнь философа
            }
        }
        eat(); //дождались разрешения, можно есть
    }

    private void think() throws InterruptedException {
        logger.debug("Философ "+philosopherId+" размышляет.");
        Thread.sleep(2000);
        logger.debug("Философ "+philosopherId+" окончил размышления.");

    }

    public int getPhilosopherId() {
        return philosopherId;
    }

    private void eat() throws InterruptedException { //философ есть

        Fork fork1;
        Fork fork2;
        logger.debug("Философу "+philosopherId+" разрешено поесть.");
        synchronized (forkMap){
            fork1 = forkMap.remove(leftForkId); //философ берет левую вилку
            if(fork1==null){
                logger.debug("Ошибка, философу "+philosopherId+" не удалось взять левую вилку. И он погиб.");
                isRunning = false;
                return;
            }
        }
        /*Здесь и ниже 2 разные секции syncronized
        * используются, чтобы подчеркнуть,
        * что взятие левой и правой вилок это 2 раздельные операции.
        * То есть возможно, 1-ый философ, которому разрешено поесть,
        * не получит правую вилку, пока какой-то другой филосов, не получит левую*/
        synchronized (forkMap){
            fork2 = forkMap.remove(rightForkId); //философ берет правую вилку
            if(fork2==null){
                logger.debug("Ошибка, философу "+philosopherId+" не удалось взять правую вилку. И он погиб.");
                isRunning = false;
                return;
            }
        }

        logger.debug("Философ "+philosopherId+" ест."+"Взяв вилки "+leftForkId+" "+rightForkId);
        Thread.sleep(1000);

        synchronized (forkMap){
            forkMap.put(rightForkId, fork1); //возвражаем правую вилку
        }

        synchronized (forkMap){
            forkMap.put(leftForkId, fork2); //возвращаем левую вилку
        }
        waiter.sendRequest(new Request(RequestType.STOP_EAT, this)); //говорит официанту, что поел
        isCanEat = false;// запрещает себе есть без разрешения официанта при следующей надобности
        logger.debug("Философ "+philosopherId+" закончил есть.");
    }

    public void stop(){
        isRunning = false;
    }

    public void canEat(){ //официант сообщает, что можно есть.
        logger.debug("Офицант дал добро философу "+philosopherId);
        isCanEat = true;
    }

}
