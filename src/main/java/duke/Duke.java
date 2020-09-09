package duke;

import duke.storage.Storage;

import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;
import duke.task.Deadline;
import duke.task.Event;

import duke.ui.Ui;

import duke.dukeException.DukeException;

import duke.parser.Parser;

public class Duke {
    public static String home = System.getProperty("user.home");
    public static String filePath = home + "/Desktop/duke.txt";
    private Storage storeFile;
    private TaskList tasks;
    private Ui ui;
    private boolean isGui = false;
    private boolean hasExit = false;
    private String userInput;

    /**
     * Initialize Duke, UI and load saved file into a TaskList object. If save file is empty, a new
     * TaskList object will be created.
     *
     * @param filePath hard-coded File Path of the saved file
     */
    public Duke(String filePath) {
        this.ui = new Ui();
        this.storeFile = new Storage(filePath);
        try {
            tasks = new TaskList(storeFile.loadFile());
        } catch (DukeException e) {
            ui.showErrorMsg(e);
            tasks = new TaskList();
        }
    }

    public Duke() {
        this(filePath);
        isGui = true;
    }

    /**
     * Main execution of Duke Chat bot for CLI
     */
    public void run() {
        ui.printMsg(ui.startupMsg());
        String[] userInputArray;
        boolean isValidInput;

        while (true) {
            userInput = ui.readInput(isGui);
            if (userInput.equals("bye")) {
                break;
            }
            switch (userInput) {
            case "list":
                listCommand();
                break;
            case "done":
                if (tasks.isEmpty()) {
                    ui.printMsg(ui.showListEmptyMsg());
                } else {
                    ui.printMsg(ui.showListMsg());
                    ui.printMsg(ui.showTaskList(tasks));
                    ;
                    do {
                        ui.printMsg(ui.showListDoneAskMsg());
                        try {
                            userInput = ui.readInput(isGui);
                            int[] tasksArray = Parser.parseMultipleTasks(userInput);
                            TaskList doneList = new TaskList();
                            for (int index : tasksArray) {
                                try {
                                    if (index > tasks.size() || index <= 0) {
                                        throw new DukeException("This task #" + index + " does not exist.");
                                    }
                                    Task task = tasks.getTask(index);
                                    task.setDone();
                                    doneList.addTask(task);
                                } catch (DukeException e) {
                                    ui.printMsg(ui.showErrorMsg(e));
                                }
                            }
                            if (!doneList.isEmpty()) {
                                ui.printMsg(ui.showListDoneMsg());
                                ui.printMsg(ui.showTaskList(doneList));
                            }
                            isValidInput = true;
                        } catch (DukeException e) {
                            isValidInput = false;
                            ui.printMsg(ui.showErrorMsg(e));
                        }
                    } while (!isValidInput);
                }

                try {
                    storeFile.saveFile(tasks);
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }

                break;
            case "find":
                if (tasks.isEmpty()) {
                    ui.printMsg(ui.showListEmptyMsg());
                } else {
                    do {
                        try {
                            ui.printMsg(ui.showFindPromptMsg());
                            userInput = ui.readInput(isGui);
                            if (userInput.equals("")) {
                                throw new DukeException("Yo! Enter a keyword.");
                            }
                            TaskList foundTasks;
                            foundTasks = tasks.findTasks(userInput);
                            if (!foundTasks.isEmpty()) {
                                ui.printMsg(ui.showFoundMsg(userInput));
                                ui.printMsg(ui.showTaskList(foundTasks));
                            } else {
                                ui.printMsg(ui.showNotFoundMsg(userInput));
                            }
                            isValidInput = true;
                        } catch (DukeException e) {
                            isValidInput = false;
                            ui.printMsg(ui.showErrorMsg(e));
                        }
                    } while (!isValidInput);
                }
                break;
            case "todo":
                do {
                    ui.printMsg(ui.showTaskAddAskMsg());
                    try {
                        userInput = ui.readInput(isGui);
                        if (userInput.equals("")) {
                            throw new DukeException("Yo! Task details are missing.");
                        }
                        Task toDo = new Todo(userInput);
                        tasks.addTask(toDo);
                        ui.printMsg(ui.showTaskAddedMsg(toDo));
                        isValidInput = true;
                    } catch (DukeException e) {
                        isValidInput = false;
                        ui.printMsg(ui.showErrorMsg(e));
                    }
                } while (!isValidInput);

                try {
                    storeFile.saveFile(tasks);
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }

                break;
            case "deadline":
                do {
                    ui.printMsg(ui.showTaskAddAskMsg());
                    try {
                        userInput = ui.readInput(isGui);
                        userInputArray = Parser.parseDetails(userInput);
                        Task deadLine = new Deadline(userInputArray[0], userInputArray[1]);
                        tasks.addTask(deadLine);
                        ui.printMsg(ui.showTaskAddedMsg(deadLine));
                        isValidInput = true;

                    } catch (DukeException e) {
                        ui.printMsg(ui.showErrorMsg(e));
                        isValidInput = false;
                    }
                } while (!isValidInput);

                try {
                    storeFile.saveFile(tasks);
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }

                break;
            case "event":
                do {
                    ui.printMsg(ui.showTaskAddAskMsg());
                    try {
                        userInput = ui.readInput(isGui);
                        userInputArray = Parser.parseDetails(userInput);
                        Task event = new Event(userInputArray[0], userInputArray[1]);
                        tasks.addTask(event);
                        ui.printMsg(ui.showTaskAddedMsg(event));
                        isValidInput = true;

                    } catch (DukeException e) {
                        ui.printMsg(ui.showErrorMsg(e));
                        isValidInput = false;
                    }
                } while (!isValidInput);

                try {
                    storeFile.saveFile(tasks);
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }

                break;
            case "delete":
                if (tasks.isEmpty()) {
                    ui.printMsg(ui.showListEmptyMsg());
                } else {
                    ui.printMsg(ui.showListMsg());
                    ui.printMsg(ui.showTaskList(tasks));
                    do {
                        ui.printMsg(ui.showTaskDeleteAskMsg());
                        try {
                            userInput = ui.readInput(isGui);
                            TaskList deletedTasks = new TaskList();
                            int[] tasksArray = Parser.parseMultipleTasks(userInput);
                            for (int index : tasksArray) {
                                try {
                                    if (index > tasks.size() || index <= 0) {
                                        throw new DukeException("This task #" + index + " does not exist.");
                                    }
                                    Task task = tasks.getTask(index);
                                    tasks.removeTask(index);
                                    deletedTasks.addTask(task);
                                } catch (DukeException e) {
                                    ui.printMsg(ui.showErrorMsg(e));
                                }
                            }
                            if (!deletedTasks.isEmpty()) {
                                ui.printMsg(ui.showTaskDeleteMsg());
                                ui.printMsg(ui.showTaskList(deletedTasks));
                            }
                            isValidInput = true;
                        } catch (DukeException e) {
                            ui.printMsg(ui.showErrorMsg(e));
                            isValidInput = false;
                        }
                    } while (!isValidInput);
                }

                try {
                    storeFile.saveFile(tasks);
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }

                break;
            case "help":
                ui.printMsg(ui.showHelp());
                break;
            default:
                try {
                    throw new DukeException("Unrecognised Command :(, type 'help' for available commands.");
                } catch (DukeException e) {
                    ui.printMsg(ui.showErrorMsg(e));
                }
            }
        }
        ui.printMsg(ui.showByeMsg());
    }

    /**
     * Main execution of Duke Chat bot for GUI
     */
    public void runGui() {
        String[] guiInputArray;

        userInput = ui.readInput(isGui);
        guiInputArray = Parser.parseCommand(userInput);
        switch (guiInputArray[0]) {
        case "bye":
            byeCommand();
            break;
        case "list":
            listCommand();
            break;
        case "done":
            doneCommand(guiInputArray);
            saveFileOperation();
            break;
        case "find":
            findCommand(guiInputArray);
            break;
        case "todo":
            todoCommand(guiInputArray);
            saveFileOperation();
            break;
        case "deadline":
            deadlineCommand(guiInputArray);
            saveFileOperation();
            break;
        case "event":
            eventCommand(guiInputArray);
            saveFileOperation();
            break;
        case "delete":
            deleteCommand(guiInputArray);
            saveFileOperation();
            break;
        case "help":
            helpCommand();
            break;
        default:
            try {
                throw new DukeException("Unrecognised Command :(, type 'help' for available commands.");
            } catch (DukeException e) {
                ui.setGuiOutput(ui.showErrorMsg(e));
            }
        }
    }

    /**
     * Duke is instantiated here.
     */
    public static void main(String[] args) {
        new Duke(filePath).run();
    }

    /**
     * Duke will run here whenever it receives a user input from MainWindow.java
     *
     * @return Duke's response after executing a command
     */
    public String getResponse() {
        runGui();
        return ui.getGuiOutput();
    }

    /**
     * A method to check whether 'bye' command is entered.
     *
     * @return boolean hasExit.
     */
    public boolean hasExit() {
        return this.hasExit;
    }

    /**
     * Operations when 'bye' command is entered.
     */
    private void byeCommand() {
        ui.setGuiOutput(ui.showByeMsg());
        hasExit = true;
    }

    /**
     * Shows the task list.
     */
    private void listCommand() {
        if (tasks.isEmpty()) {
            ui.setGuiOutput(ui.showListEmptyMsg());
        } else {
            ui.setGuiOutput(ui.showListMsg() + "\n" + ui.showTaskList(tasks));
        }
    }

    /**
     * Mark tasks as done when 'done <tasks>' command is entered. Multiple tasks operation is supported.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void doneCommand(String[] guiInputArray) {
        if (tasks.isEmpty()) {
            ui.setGuiOutput(ui.showListEmptyMsg());
        } else {
            try {
                if (guiInputArray.length <= 1) {
                    throw new DukeException("Yo! Enter the task number(s).");
                }
                userInput = guiInputArray[1];
                int[] tasksArray = Parser.parseMultipleTasks(userInput);
                TaskList doneList = new TaskList();
                for (int index : tasksArray) {
                    try {
                        if (index > tasks.size() || index <= 0) {
                            throw new DukeException("This task #" + index + " does not exist.");
                        }
                        Task task = tasks.getTask(index);
                        task.setDone();
                        doneList.addTask(task);
                    } catch (DukeException e) {
                        ui.setGuiOutput(ui.showErrorMsg(e));
                    }
                }
                if (!doneList.isEmpty()) {
                    ui.setGuiOutput(ui.showListDoneMsg() + "\n" + ui.showTaskList(doneList));
                }
            } catch (DukeException e) {
                ui.setGuiOutput(ui.showErrorMsg(e));
            }
        }
    }

    /**
     * Find tasks that match they keyword entered.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void findCommand(String[] guiInputArray) {
        if (tasks.isEmpty()) {
            ui.setGuiOutput(ui.showListEmptyMsg());
        } else {
            try {
                if (guiInputArray.length <= 1 || guiInputArray[1].isBlank()) {
                    throw new DukeException("Yo! Enter a keyword.");
                }
                userInput = guiInputArray[1];
                TaskList foundTasks;
                foundTasks = tasks.findTasks(userInput);
                if (!foundTasks.isEmpty()) {
                    ui.setGuiOutput(ui.showFoundMsg(userInput) + "\n" + ui.showTaskList(foundTasks));
                } else {
                    ui.setGuiOutput(ui.showNotFoundMsg(userInput));
                }
            } catch (DukeException e) {
                ui.setGuiOutput(ui.showErrorMsg(e));
            }
        }
    }

    /**
     * Add a todo task to the task list.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void todoCommand(String[] guiInputArray) {
        try {
            if (guiInputArray.length <= 1 || guiInputArray[1].isBlank()) {
                throw new DukeException("Yo! Task details are missing.");
            }
            userInput = guiInputArray[1];
            Task toDo = new Todo(userInput);
            tasks.addTask(toDo);
            ui.setGuiOutput(ui.showTaskAddedMsg(toDo));
        } catch (DukeException e) {
            ui.setGuiOutput(ui.showErrorMsg(e));
        }
    }

    /**
     * Add a deadline task to the task list.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void deadlineCommand(String[] guiInputArray) {
        String[] userInputArray;
        try {
            if (guiInputArray.length <= 1) {
                throw new DukeException("Yo! Command Syntax Error. '<Details> /by or /at <dd/MM/yy [HH:MM]>'");
            }
            userInput = guiInputArray[1];
            userInputArray = Parser.parseDetails(userInput);
            Task deadLine = new Deadline(userInputArray[0], userInputArray[1]);
            tasks.addTask(deadLine);
            ui.setGuiOutput(ui.showTaskAddedMsg(deadLine));
        } catch (DukeException e) {
            ui.setGuiOutput(ui.showErrorMsg(e));
        }
    }

    /**
     * Add a event task to the task list.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void eventCommand(String[] guiInputArray) {
        String[] userInputArray;
        try {
            if (guiInputArray.length <= 1) {
                throw new DukeException("Yo! Command Syntax Error. '<Details> /by or /at <dd/MM/yy [HH:MM]>'");
            }
            userInput = guiInputArray[1];
            userInputArray = Parser.parseDetails(userInput);
            Task event = new Event(userInputArray[0], userInputArray[1]);
            tasks.addTask(event);
            ui.setGuiOutput(ui.showTaskAddedMsg(event));
        } catch (DukeException e) {
            ui.setGuiOutput(ui.showErrorMsg(e));
        }
    }

    /**
     * Mark tasks for deletion when 'delete <tasks>' command is entered. Multiple tasks operation is supported.
     *
     * @param guiInputArray input from the GUI dialog box.
     */
    private void deleteCommand(String[] guiInputArray) {
        if (tasks.isEmpty()) {
            ui.setGuiOutput(ui.showListEmptyMsg());
        } else {
            try {
                if (guiInputArray.length <= 1) {
                    throw new DukeException("Yo! Enter the task numbers(s).");
                }
                userInput = guiInputArray[1];
                TaskList deletedTasks = new TaskList();
                int[] tasksArray = Parser.parseMultipleTasks(userInput);
                for (int index : tasksArray) {
                    try {
                        if (index > tasks.size() || index <= 0) {
                            throw new DukeException("This task #" + index + " does not exist.");
                        }
                        Task task = tasks.getTask(index);
                        tasks.removeTask(index);
                        deletedTasks.addTask(task);
                    } catch (DukeException e) {
                        ui.setGuiOutput(ui.showErrorMsg(e));
                    }
                }
                if (!deletedTasks.isEmpty()) {
                    ui.setGuiOutput(ui.showTaskDeleteMsg() + "\n" + ui.showTaskList(deletedTasks));
                }
            } catch (DukeException e) {
                ui.setGuiOutput(ui.showErrorMsg(e));
            }
        }
    }

    /**
     * Shows a help list of available commands.
     */
    private void helpCommand() {
        ui.setGuiOutput(ui.showHelp());
    }

    /**
     * A method for file saving operations for commands that edit the task list.
     */
    private void saveFileOperation() {
        try {
            storeFile.saveFile(tasks);
        } catch (DukeException e) {
            ui.setGuiOutput(ui.showErrorMsg(e));
        }
    }
}
