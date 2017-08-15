package com.nlp.text2graph;

import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * Hello world!
 *
 */
public class App {

	/* graph graphics */
	protected static String styleSheet = "node {shape: box;fill-color: red;text-size: 18px;size:30px;} node.marked {"
			+ "	fill-color: red; } edge {fill-color: black;text-size: 18px;size: 1.5px;text-color:blue;}";

	public static void main(String[] args) {

		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[] { "-maxLength", "80", "-retainTmpSubcategories" });

		String sentence = "The quick brown fox jumps over the lazy dog";
		Tree parse = lp.parse(sentence);

		System.out.println();

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

		int count = 0;
		Graph depenGraph = new SingleGraph("Stanford Dependencies Parser");
		depenGraph.setAutoCreate(true);

		for (TypedDependency tdlElement : tdl) {

			try {
				String startNodeLabel = tdlElement.gov().toString();
				String endNodeLabel = tdlElement.dep().toString();
				String relationLabel = tdlElement.reln().toString();

				if (depenGraph.getNode(startNodeLabel) == null) {
					depenGraph.addNode(startNodeLabel).setAttribute("label", startNodeLabel);
				}

				if (depenGraph.getNode(endNodeLabel) == null) {
					depenGraph.addNode(endNodeLabel).setAttribute("label", endNodeLabel);
				}

				depenGraph.addEdge(String.valueOf(count), startNodeLabel, endNodeLabel, true).setAttribute("label",
						relationLabel);
			} catch (Exception e) {
				continue;
			}

			System.out.println(count + " - " + tdlElement.gov() + " - " + tdlElement.reln() + " - " + tdlElement.dep());
			count++;
		}

		depenGraph.addAttribute("ui.stylesheet", styleSheet);
		org.graphstream.ui.view.Viewer viewer = depenGraph.display();
		viewer.enableAutoLayout();

		System.out.println(tdl);

	}
}
