package com.dukeacademy.model.question;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dukeacademy.testutil.QuestionBuilder;

public class TitleContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TitleContainsKeywordsPredicate
            firstPredicate = new TitleContainsKeywordsPredicate(firstPredicateKeywordList);
        TitleContainsKeywordsPredicate
            secondPredicate = new TitleContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TitleContainsKeywordsPredicate
            firstPredicateCopy = new TitleContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different question -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        TitleContainsKeywordsPredicate
            predicate = new TitleContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new QuestionBuilder().withTitle("Alice Bob").build()));

        // Multiple keywords
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new QuestionBuilder().withTitle("Alice Bob").build()));

        // Only one matching keyword
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new QuestionBuilder().withTitle("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new QuestionBuilder().withTitle("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        TitleContainsKeywordsPredicate
            predicate = new TitleContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new QuestionBuilder().withTitle("Alice").build()));

        // Non-matching keyword
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new QuestionBuilder().withTitle("Alice Bob").build()));

        // Keywords match topic, status and difficulty, but does not match name
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("12345", "alice@status.com", "Main", "Street"));
        assertFalse(predicate.test(new QuestionBuilder()
            .withTitle("Alice")
            .withTopic("12345").withStatus("alice@status.com").withDifficulty("Main Street").build()));
    }
}
