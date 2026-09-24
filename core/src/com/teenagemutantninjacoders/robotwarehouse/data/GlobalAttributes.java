package com.teenagemutantninjacoders.robotwarehouse.data;

/**
 * Created by JordiRM on 19/02/2016.
 */
public class GlobalAttributes {

    public enum COLOR {
        NONE("none"), RED("red"), BLUE("blue"), YELLOW("yellow"), GREEN("green"), ORANGE("orange"), PURPLE("purple"), ANY("any");
        private String value;

        COLOR(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public int getIntValue() {
            switch (value) {
                case "red":
                    return 1;
                case "blue":
                    return 2;
                case "yellow":
                    return 3;
                case "green":
                    return 4;
                case "orange":
                    return 5;
                case "purple":
                    return 6;
                default:
                    return 0;
            }
        }

        public static COLOR fromString(String text) {
            if (text != null) {
                for (COLOR var : COLOR.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return BLUE;
        }
    }
    public enum POWER {
        POWER_DISABLE_CANON("disable_canon"),POWER_DISABLE_ROBOTS("disable_robots"), POWER_FUMIGATION("fumigation"), POWER_NONE("none");
        private String value;

        POWER(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static POWER fromString(String text) {
            if (text != null) {
                for (POWER var : POWER.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return POWER_NONE;
        }
    }
    public enum CHALLENGE {
        NONE("none"),NO_LOSSED_BOX("no_lossed_box"), BOX_COMBO("box_combo"), TIME_ELAPSED("time_elapsed"),
        MAX_MOVES ("max_moves"), LAST_COLOR("last_color"), BAD_COLOR("bad_color"),
        ENEMY_SAFE("enemy_safe"), NO_ENEMY("no_enemy"), SMASH_ENEMY("smash_enemy"),
        NO_POWERS("no_powers"), NO_BARRIER("no_barrier");
        private String value;

        CHALLENGE(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static CHALLENGE fromString(String text) {
            if (text != null) {
                for (CHALLENGE var : CHALLENGE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }

    public enum CHALLENGE_OBJECT {
        NONE("none"), ALL("all"), RED("red"), BLUE("blue"), YELLOW("yellow"),GREEN("green"), ORANGE("orange"), PURPLE("purple"), ANY("any"),
        ROBOT("robot"), PUSHER_ROBOT("pusher_robot"), EXPLOSIVE_ROBOT("explosive_robot"), RATLIEN("ratlien");
        private String value;

        CHALLENGE_OBJECT(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static CHALLENGE_OBJECT fromString(String text) {
            if (text != null) {
                for (CHALLENGE_OBJECT var : CHALLENGE_OBJECT.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }

    public enum ACTION_ON_TARGET{
        NONE, PUSH, EXPLODE, INFEST
    }
    
    public enum ACTIVATION_CONDITION {
        NORMAL, ALWAYS, NEVER
    }

    public enum LEVEL_TYPE{
        NORMAL("normal"), STARS("stars"), SPECIAL_CHALLENGE("special_challenge");
        private String value;
        LEVEL_TYPE(String newValue) {
            setValue(newValue);
        }
        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static LEVEL_TYPE fromString(String text) {
            if (text != null) {
                for (LEVEL_TYPE var : LEVEL_TYPE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NORMAL;
        }
    }

    public enum EPISODE_SPECIAL_EVENT {
        NONE, BASE_EPISODES_COMPLETED
    }
}

