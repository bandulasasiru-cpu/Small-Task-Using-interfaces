import java.util.*;
interface WaterLevelObserver {
    public void update(int waterLevel);
}

class Alarm implements WaterLevelObserver {
    private String id;

    Alarm(String id) {
        this.id = id;
    }

    @Override
    public void update(int waterLevel) {
        System.out.println(
            waterLevel > 50
                ? id + "-->Alarm ON"
                : id + "-->Alarm OFF"
        );
    }
}

class Splitter implements WaterLevelObserver {
    private String id;

    Splitter(String id) {
        this.id = id;
    }

    @Override
    public void update(int waterLevel) {
        System.out.println(waterLevel > 75? id + "-->Splitter ON" : id + "-->Splitter OFF");
    }
}

class Display implements WaterLevelObserver {
    private String id;

    Display(String id) {
        this.id = id;
    }

    @Override
    public void update(int waterLevel) {
        System.out.println(id + "-->Water Level : " + waterLevel);
    }
}

class SMSSender implements WaterLevelObserver {
    private String id;

    SMSSender(String id) {
        this.id = id;
    }

    @Override
    public void update(int waterLevel) {
        System.out.println(id + "-->Sending SMS..." + waterLevel);
    }
}

class ControlRoom {
    private WaterLevelObserver[] waterLevelObserverArray =
        new WaterLevelObserver[0];

    private int waterLevel;

    public void addWaterLevelObserver(
            WaterLevelObserver waterLevelObserver) {

        extendArray();

        waterLevelObserverArray[
            waterLevelObserverArray.length - 1
        ] = waterLevelObserver;
    }

    public void setWaterLevel(int waterLevel) {
        if (this.waterLevel != waterLevel) {
            this.waterLevel = waterLevel;
            notifyDevices();
        }
    }

    private void notifyDevices() {
        for (WaterLevelObserver ob : waterLevelObserverArray) {
            ob.update(waterLevel);
        }
    }

    private void extendArray() {
        WaterLevelObserver[] tempWaterLevelObserverArray =
            new WaterLevelObserver[waterLevelObserverArray.length + 1];

        for (int i = 0; i < waterLevelObserverArray.length; i++) {
            tempWaterLevelObserverArray[i] =
                waterLevelObserverArray[i];
        }

        waterLevelObserverArray = tempWaterLevelObserverArray;
    }
}

class Demo {
    public static void main(String[] args) {

        ControlRoom controlRoom = new ControlRoom();

        controlRoom.addWaterLevelObserver(new Alarm("A0001"));
        controlRoom.addWaterLevelObserver(new Display("D0001"));
        controlRoom.addWaterLevelObserver(new SMSSender("SM0001"));
        controlRoom.addWaterLevelObserver(new Splitter("SP001"));

        Random r = new Random();

        while (true) {
            int waterLevel = r.nextInt(101); // 0 to 100

            controlRoom.setWaterLevel(waterLevel);

            System.out.println();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

