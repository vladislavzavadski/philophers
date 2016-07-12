package by.training.philosopers.util;

import by.training.philosopers.domain.Request;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vladislav on 11.07.16.
 */
public class Waiter implements Runnable{
    private List<Request> requests = new LinkedList<>();//список входящих запросов для официанта
    private volatile boolean isRunning = true;
    private List<Philosopher> waitingPhilosophers = new LinkedList<>();//список философов, ожидающих разрешения пояесть
    private List<Philosopher> eatingPhilosophers = new LinkedList<>();//список философов, которые уже едят

    @Override
    public void run() {
        while (isRunning){
            if(!requests.isEmpty()) { //проверяем есть ли новые запрсосы
                Request request;
                synchronized (requests) {
                    request = requests.remove(0); //извлекаем запрос, который получен раньше всех
                }
                switch (request.getRequestType()) {
                    case WANT_TO_EAT: {
                        requestForEating(request.getPhilosopher());//философ сказал, что он хочет поесть
                        break;
                    }
                    case STOP_EAT: {
                        stopEating(request.getPhilosopher());//философ сообщил официанту, что он уже поел
                        break;
                    }
                }
            }
        }
    }

    private void requestForEating(Philosopher philosopher){ //философ хочет поесть

        if(eatingPhilosophers.size()<2&&!isNeighborEat(philosopher)){
            eatingPhilosophers.add(philosopher); //если сейчас едят меньше 2-х человек и ни один из соседей сейчас не ест, то сразу даем возможность философу поесть
            philosopher.canEat();
        }else {
            waitingPhilosophers.add(philosopher); //Если невозможно сейчас посадить философа за стол, добавляем его в список ожидания
        }
    }

    public void sendRequest(Request request){ //Философ подает новый запрос официанту
        synchronized (requests){
            requests.add(request); //помещаем запрос в очередь запросов официанта
        }
    }

    private void stopEating(Philosopher philosopher){//философ закончил есть
        eatingPhilosophers.remove(philosopher); //убираем философа из списка "едящих"
        for(int i=0; i<waitingPhilosophers.size(); i++){//пробегаем с начала списка ожидающих философов к его концу(в начале списка находятся философы, которые раньше всех захотели есть)
            Philosopher temp = waitingPhilosophers.get(i);
            if(!isNeighborEat(temp)){ //если возможно посадить нового философа за стол (ни один из соседей не ест)
                waitingPhilosophers.remove(i);//удаляем его из списка ожидания
                eatingPhilosophers.add(temp);//добавляем в список кушающих философов
                temp.canEat(); //сообщаем философу, что он может поесть
                return; //поскольку освободилось 1 место за столом, то и посадить можно максимум 1-го философа
            }
        }
    }

    private boolean isNeighborEat(Philosopher philosopher){//метод проверки, "А не едят ли соседи философа?"
        int leftNeighborId = philosopher.getPhilosopherId()==1?5:philosopher.getPhilosopherId()-1;//получаем номер соседа слева
        int rightNeighborId = philosopher.getPhilosopherId()==5?1:philosopher.getPhilosopherId()+1;//и номер соседа справа
        for(Philosopher temp:eatingPhilosophers){
            int tempPhilosopherId = temp.getPhilosopherId();
            if(tempPhilosopherId==leftNeighborId||tempPhilosopherId==rightNeighborId){//если хоть один сосед ест, не пускаем философа за стол
                return true;
            }
        }
        return false;
    }

    public void stop(){
        isRunning = false;
    }


}
