package com.t13g2.forum.model;

import java.util.function.Predicate;

import com.t13g2.forum.model.person.Person;

import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyForumBook newData);

    /** Returns the ForumBook */
    ReadOnlyForumBook getForumBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void updatePerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoForumBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoForumBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoForumBook();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoForumBook();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitForumBook();
}
