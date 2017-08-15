package com.nlp.text2graph.main;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import com.nlp.text2graph.adapter.file.TextFileAdapter;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class AppDoc2Graph {

	/* graph graphics */
	protected static String generalStyleSheet = "node {shape: box;fill-color: red;text-size: 18px;size:20px;}"
			+ " node#ROOT {fill-color: green; size: 30px;} " + " node.marked {fill-color: red; } "
			+ " edge {fill-color: black;text-size: 18px;size: 1.5px;text-color:blue;}";

	private static TextFileAdapter textFileAdapter = new TextFileAdapter();
	private static TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	private static GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	private static LexicalizedParser lp = LexicalizedParser
			.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		String docInputPath = System.getProperty("user.dir") + "/data/input.txt";
		String paragraph = textFileAdapter.parseSingleFileToString(docInputPath);
		lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });

		Reader reader = new StringReader(paragraph);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		List<String> sentenceList = new ArrayList<String>();

		Graph dGraph = new SingleGraph("Stanford Dependencies Parser");

		for (List<HasWord> sentence : dp) {
			sentenceList.add(Sentence.listToString(sentence));
		}

		int count = 0;

		for (String sentence : sentenceList) {

			Tree parse = lp.parse(sentence);
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			List<TypedDependency> tdls = gs.typedDependenciesCCprocessed();

			for (TypedDependency tdl : tdls) {

				try {

					String startNodeLabel = tdl.gov().toString();
					String endNodeLabel = tdl.dep().toString();
					String relationLabel = tdl.reln().toString();

					if (dGraph.getNode(startNodeLabel) == null) {
						dGraph.addNode(startNodeLabel).setAttribute("label", startNodeLabel);
					}

					if (dGraph.getNode(endNodeLabel) == null) {
						dGraph.addNode(endNodeLabel).setAttribute("label", endNodeLabel);
					}

					dGraph.addEdge(String.valueOf(count), startNodeLabel, endNodeLabel, true).setAttribute("label",
							relationLabel);
				} catch (Exception e) {
					continue;
				}

				System.out.println(count + " - " + tdl.gov() + " - " + tdl.reln() + " - " + tdl.dep());
				count++;

				count++;

			}

		}

		dGraph.addAttribute("ui.stylesheet", generalStyleSheet);
		Viewer viewer = dGraph.display();
		viewer.enableAutoLayout();

	}

}
