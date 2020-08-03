package ru.geekbrains.common;


import ru.geekbrains.common.command.*;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    private CommandType type;
    private Object data;

    public Object getData() {
        return data;
    };

    public CommandType getType() {
        return type;
    }

    public static Command NavigateCommand(List<File> navigates){
        Command command = new Command();
        command.type = CommandType.NAVIGATE;
        command.data = new NavigateCommand(navigates);
        return command;
    }

    public static Command GetFileCommand(File srcfile, File dstfile){
        Command command = new Command();
        command.type = CommandType.GET_FILE;
        command.data = new GetFileCommand(srcfile, dstfile);
        return command;
    }

    public static Command PutFileCommand(File srcfile, File dstfile){
        Command command = new Command();
        command.type = CommandType.PUT_FILE;
        command.data = new PutFileCommand(srcfile, dstfile);
        return command;
    }

    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommand(errorMessage);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

}
