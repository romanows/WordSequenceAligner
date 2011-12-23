# Overview
WordSequenceAligner is a Java class that aligns two string sequences  
and calculates metrics such as word error rate (WER).  Pretty-   
printing enables human-readable logging of alignments and metrics.  

This class is intended to reproduce the main functionality of the   
<a href="http://www.icsi.berkeley.edu/Speech/docs/sctk-1.2/sclite.htm">NIST sclite tool</a>.   
The Sphinx 4 source for the class <a href="http://cmusphinx.sourceforge.net/sphinx4/javadoc/edu/cmu/sphinx/util/NISTAlign.html">edu.cmu.sphinx.util.NISTAlign<a/>   
was referenced when writing the WordSequenceAligner code.
  
Feedback and bugfixes are welcomed.  

Brian Romanowski  
romanows@gmail.com  


# Details
This code is licensed under one of the BSD variants, please see   
LICENSE.txt for full details.  


# Example
    WordSequenceAligner werEval = new WordSequenceAligner();
    String [] ref = "the quick brown cow jumped over the moon".split(" ");
    String [] hyp = "quick brown cows jumped way over the moon dude".split(" ");
    Alignment a = werEval.align(ref, hyp);
    System.out.println(a);

Produces the output:

            # seq  # ref   # hyp   # cor   # sub   # ins   # del   acc     WER     # seq cor
    STATS:  1      8       9       6       1       2       1       0.75    0.5     0
    -----   -----  -----   -----   -----   -----   -----   -----   -----   -----   -----	
    REF:    THE    quick   brown   COW     jumped  ***     over    the     moon    ****
    HYP:    ***    quick   brown   COWS    jumped  WAY     over    the     moon    DUDE

Where the top portion of the output are the statistics for the given  
pair of reference/hypothesis sentences, and the lower portion   
displays the alignment, visually. 