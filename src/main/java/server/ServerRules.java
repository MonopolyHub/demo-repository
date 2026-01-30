package server;

import DTO.GameStateDTO;
import common.MessageType;


public class ServerRules {

    public static MessageType[] allowedCommandsFor(GameStateDTO state) {
        if (state.finished) {
            return new MessageType[0];
        }
        String phase = state.phase;
        if (phase == null) {
            return new MessageType[0];
        }

        boolean canBuild = state.canBuildHouse || state.canBuildHotel;

        if ("TURN_START".equals(phase)) {
            if (canBuild) {
                return new MessageType[]{
                        MessageType.ROLL_DICE,
                        MessageType.BUILD,
                        MessageType.UNDO,
                        MessageType.REDO
                };
            }
            return new MessageType[]{
                    MessageType.ROLL_DICE,
                    MessageType.UNDO,
                    MessageType.REDO
            };
        }

        if ("DECISION".equals(phase)) {
            if (state.canBuyProperty) {
                if (canBuild) {
                    return new MessageType[]{
                            MessageType.BUY_PROPERTY,
                            MessageType.BUILD,
                            MessageType.UNDO,
                            MessageType.END_TURN
                    };
                }
                return new MessageType[]{
                        MessageType.BUY_PROPERTY,
                        MessageType.UNDO,
                        MessageType.END_TURN
                };
            }

            if (canBuild) {
                return new MessageType[]{MessageType.BUILD, MessageType.END_TURN, MessageType.UNDO};
            }
            return new MessageType[]{MessageType.END_TURN, MessageType.UNDO};
        }

        if ("TURN_END".equals(phase)) {
            if (canBuild) {
                return new MessageType[]{
                        MessageType.BUILD,
                        MessageType.UNDO,
                        MessageType.END_TURN
                };
            }
            return new MessageType[]{
                    MessageType.UNDO,
                    MessageType.END_TURN
            };
        }

        return new MessageType[0];
    }
}
