package com.pwnetics.metric;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.pwnetics.metric.WordSequenceAligner.Alignment;
import com.pwnetics.metric.WordSequenceAligner.SummaryStatistics;

public class TestWordSequenceAligner {

	@Test
	public void testAlignListOfStringListOfString() {
		WordSequenceAligner werEval = new WordSequenceAligner();
		List<Alignment> alignments = new ArrayList<WordSequenceAligner.Alignment>();
		
		// Reference alignments and stats created with the NIST sclite tool, default settings
		alignments.add(werEval.align("the quick brown cow jumped over the moon".split(" "), "quick brown cows jumped way over the moon dude".split(" ")));
		alignments.add(werEval.align("the quick brown cow jumped over the moon".split(" "), "quick brown over the moon dude".split(" ")));
		alignments.add(werEval.align("0 1 1 0 0 1 0 0 0 1 1 1 0 0 0 0 1 1 1 0 1 1 0 1 0 1 0 1 1 1 0 0 0 0 1 1 1 0 0 1 0 1 1 0 1 0 1 1 0 1 0 0 0 1 0 1 0 0 1 0 1 0 1 1 1 0 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 0 1 1 1 1 1 0 1 0 1 0 0 1 0 0 1 1 0 1".split(" "), "1 0 1 1 0 1 1 1 1 0 0 0 0 0 1 0 0 0 1 1 0 0 1 1 1 0 0 0 0 0 1 0 0 1 0 1 1 0 0 1 1 0 0 1 1 1 1 0 0 0 1 0 1 1 0 0 1 1 1 0 1 0 0 0 1 0 0 1 1 0 0 1 0 1 1 1 1 1 0 0 1 0 0 0 1 1 1 0 1 1 1 0 1 0 1 1 1 0 0 1".split(" ")));
		
		SummaryStatistics ss = werEval.new SummaryStatistics(alignments);
		assertTrue(ss.getNumSentences() == 3);
		assertTrue(ss.getNumReferenceWords() == 116);
		assertTrue(ss.getNumHypothesisWords() == 115);
		assertEquals(0.776, ss.getCorrectRate(), 0.001);
		assertEquals(0.103, ss.getSubstitutionRate(), 0.001);
		assertEquals(0.121, ss.getDeletionRate(), 0.001);
		assertEquals(0.112, ss.getInsertionRate(), 0.001);
		assertEquals(0.336, ss.getWordErrorRate(), 0.001);
		assertEquals(1.000, ss.getSentenceErrorRate(), 0.001);
	}

	@Test
	public void testAlignStringArrayStringArray() {
		WordSequenceAligner werEval = new WordSequenceAligner();
		
		Alignment a;
		a = werEval.align(new String[] {}, new String[] {});
		assertTrue(Arrays.equals(a.reference, new String[] {}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {}));
		assertTrue(a.getHypothesisLength() == 0);
		assertTrue(a.getReferenceLength() == 0);
		assertTrue(a.getHypothesisLength() == 0);
		assertTrue(a.numDeletions == 0);
		assertTrue(a.numInsertions == 0);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 0);
		assertTrue(a.isSentenceCorrect());
		
		// This is fine, the empty string is treated as a word because it is a non-null element in the array
		a = werEval.align(new String[] {}, new String[] {""});
		assertTrue(Arrays.equals(a.reference, new String[] {null}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {""}));
		assertTrue(a.getReferenceLength() == 0);
		assertTrue(a.getHypothesisLength() == 1);
		assertTrue(a.numDeletions == 0);
		assertTrue(a.numInsertions == 1);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 0);
		assertTrue(!a.isSentenceCorrect());

		a = werEval.align(new String[] {""}, new String[] {});
		assertTrue(Arrays.equals(a.reference, new String[] {""}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {null}));
		assertTrue(a.getReferenceLength() == 1);
		assertTrue(a.getHypothesisLength() == 0);
		assertTrue(a.numDeletions == 1);
		assertTrue(a.numInsertions == 0);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 0);
		assertTrue(!a.isSentenceCorrect());

		a = werEval.align(new String[] {""}, new String[] {""});
		assertTrue(Arrays.equals(a.reference, new String[] {""}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {""}));
		assertTrue(a.getReferenceLength() == 1);
		assertTrue(a.getHypothesisLength() == 1);
		assertTrue(a.numDeletions == 0);
		assertTrue(a.numInsertions == 0);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 1);
		assertTrue(a.isSentenceCorrect());

		a = werEval.align(new String[] {"b"}, new String[] {"a"});
		assertTrue(Arrays.equals(a.reference, new String[] {"B"}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {"A"}));
		assertTrue(a.getReferenceLength() == 1);
		assertTrue(a.getHypothesisLength() == 1);
		assertTrue(a.numDeletions == 0);
		assertTrue(a.numInsertions == 0);
		assertTrue(a.numSubstitutions == 1);
		assertTrue(a.getNumCorrect() == 0);
		assertTrue(!a.isSentenceCorrect());

		a = werEval.align(new String[] {"b"}, new String[] {"a","c"});
		assertTrue(Arrays.equals(a.reference, new String[] {null,"B"}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {"A","C"}));
		assertTrue(a.getReferenceLength() == 1);
		assertTrue(a.getHypothesisLength() == 2);
		assertTrue(a.numDeletions == 0);
		assertTrue(a.numInsertions == 1);
		assertTrue(a.numSubstitutions == 1);
		assertTrue(a.getNumCorrect() == 0);
		assertTrue(!a.isSentenceCorrect());

		a = werEval.align(new String[] {"b","c"}, new String[] {"a","b"});
		assertTrue(Arrays.equals(a.reference, new String[] {null,"b","C"}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {"A","b",null}));
		assertTrue(a.getReferenceLength() == 2);
		assertTrue(a.getHypothesisLength() == 2);
		assertTrue(a.numDeletions == 1);
		assertTrue(a.numInsertions == 1);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 1);
		assertTrue(!a.isSentenceCorrect());

		
		// Reference alignments below created with the NIST sclite tool, default settings
		a = werEval.align("the quick brown cow jumped over the moon".split(" "), "quick brown cows jumped way over the moon dude".split(" "));
		assertTrue(Arrays.equals(a.reference, new String[] {"THE","quick","brown","COW","jumped",null,"over","the","moon",null}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {null,"quick","brown","COWS","jumped","WAY","over","the","moon","DUDE"}));
		assertTrue(a.getReferenceLength() == 8);
		assertTrue(a.getHypothesisLength() == 9);
		assertTrue(a.numDeletions == 1);
		assertTrue(a.numInsertions == 2);
		assertTrue(a.numSubstitutions == 1);
		assertTrue(a.getNumCorrect() == 6);
		assertTrue(!a.isSentenceCorrect());
		
		a = werEval.align("the quick brown cow jumped over the moon".split(" "), "quick brown over the moon dude".split(" "));
		assertTrue(Arrays.equals(a.reference, new String[] {"THE","quick","brown","COW","JUMPED","over","the","moon",null}));
		assertTrue(Arrays.equals(a.hypothesis, new String[] {null,"quick","brown",null,null,"over","the","moon","DUDE"}));
		assertTrue(a.getReferenceLength() == 8);
		assertTrue(a.getHypothesisLength() == 6);
		assertTrue(a.numDeletions == 3);
		assertTrue(a.numInsertions == 1);
		assertTrue(a.numSubstitutions == 0);
		assertTrue(a.getNumCorrect() == 5);
		assertTrue(!a.isSentenceCorrect());

		a = werEval.align("0 1 1 0 0 1 0 0 0 1 1 1 0 0 0 0 1 1 1 0 1 1 0 1 0 1 0 1 1 1 0 0 0 0 1 1 1 0 0 1 0 1 1 0 1 0 1 1 0 1 0 0 0 1 0 1 0 0 1 0 1 0 1 1 1 0 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 0 1 1 1 1 1 0 1 0 1 0 0 1 0 0 1 1 0 1".split(" "), "1 0 1 1 0 1 1 1 1 0 0 0 0 0 1 0 0 0 1 1 0 0 1 1 1 0 0 0 0 0 1 0 0 1 0 1 1 0 0 1 1 0 0 1 1 1 1 0 0 0 1 0 1 1 0 0 1 1 1 0 1 0 0 0 1 0 0 1 1 0 0 1 0 1 1 1 1 1 0 0 1 0 0 0 1 1 1 0 1 1 1 0 1 0 1 1 1 0 0 1".split(" "));
		assertTrue(a.getReferenceLength() == 100);
		assertTrue(a.getHypothesisLength() == 100);
		assertTrue(a.numDeletions == 10);
		assertTrue(a.numInsertions == 10);
		assertTrue(a.numSubstitutions == 11);
		assertTrue(a.getNumCorrect() == 79);
		assertTrue(!a.isSentenceCorrect());
	}
}
