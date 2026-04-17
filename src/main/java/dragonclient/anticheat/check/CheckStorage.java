package dragonclient.anticheat.check;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dragonclient.anticheat.check.impl.GroundCheck;
import dragonclient.anticheat.check.impl.InvalidCheck;
import dragonclient.anticheat.check.impl.MoveCheck;
import dragonclient.anticheat.check.impl.StepCheck;
import dragonclient.anticheat.data.PlayerData;

public class CheckStorage {

    private static CheckStorage instance;

    private final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();
    Class<?>[] checkClasses = new Class[] {
            InvalidCheck.class,
            StepCheck.class,
            GroundCheck.class,
            MoveCheck.class,
    };

    public void init() {
        instance = this;

        for(Class<?> clazz : checkClasses) {
            try {
                CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Check> loadChecks(PlayerData data) {
        List<Check> checkList = new ArrayList<>();

        for(Constructor<?> constructor : CONSTRUCTORS) {
            try {
                Check check = (Check) constructor.newInstance(data);

                checkList.add(check);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return checkList;
    }

    public List<Class<?>> getChecks() {
        return Arrays.asList(checkClasses);
    }

    public static CheckStorage getInstance() {
        return instance;
    }

    public static void setInstance(CheckStorage instance) {
        CheckStorage.instance = instance;
    }
}
