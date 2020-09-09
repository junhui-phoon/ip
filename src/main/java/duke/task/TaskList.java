package duke.task;

import duke.dukeException.DukeException;

import java.util.ArrayList;

/**
 * Represents a Task List.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructor of a TaskList object with an existing ArrayList, for e.g. save file.
     *
     * @param list ArrayList of Tasks.
     */
    public TaskList(ArrayList<Task> list) {
        this.tasks = list;
    }

    /**
     * Constructor of a TaskList object without any existing ArrayList, for e.g. no save file.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * A method to obtain a specified Task object from TaskList.
     *
     * @param id Specified task number from user input.
     * @return Task object.
     * @throws DukeException If task number is out of bounds in the ArrayList.
     */
    public Task getTask(int id) throws DukeException {
        try {
            return tasks.get(id - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException(" This task #" + id + " does not exist.");
        }
    }

    /**
     * A method to add a new Task object into TaskList.
     *
     * @param t Task object.
     */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /**
     * A method to remove a new Task object into TaskList.
     *
     * @param id Task number from user input.
     */
    public void removeTask(int id) {
        tasks.remove(id - 1);
    }

    /**
     * A method to indicate whether the TaskList is empty.
     *
     * @return Boolean 'true' or 'false'.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * A method to return the size of the TaskList.
     *
     * @return Integer, size of list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * A method to retrieve the TaskList.
     *
     * @return ArrayList of TaskList.
     */
    public ArrayList<Task> getList() {
        return this.tasks;
    }

    /**
     * A method to find a task from the TaskList using a keyword
     *
     * @param keyword
     * @return
     */
    public TaskList findTasks(String keyword) {
        TaskList foundTasks = new TaskList();
        for (Task task : tasks) {
            if (task.getTaskName().contains(keyword)) {
                foundTasks.addTask(task);
            }
        }
        return foundTasks;
    }
}
