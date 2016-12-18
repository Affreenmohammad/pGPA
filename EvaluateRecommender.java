package com.predictionmarketing.RecommenderApp;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class EvaluateRecommender {

	public static void main(String[] args) throws Exception {
		DataModel model = new FileDataModel(new File("data/datapgpa.csv"));
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		System.out.println(result);

	}

}

class MyRecommenderBuilder implements RecommenderBuilder {
	
	public Recommender buildRecommender (DataModel dataModel) throws TasteException {	
		CourseList courses = new CourseList();
		GradeList grades= new GradeList();
		String coursename="";
		String gradename = "";int i;
		HashMap<String ,String> courselist = new HashMap<String ,String >();
		HashMap<Integer ,String> gradelist = new HashMap<Integer ,String >();
		courselist = courses.getInstance();
		gradelist = grades.getInstance();
		// create an ItemSimilarity object for testing similarity
		//ItemSimilarity sim = new LogLikelihoodSimilarity(dataModel); 
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.9, similarity, dataModel);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
		//Recommender recommender = new GenericItemBasedRecommender(dataModel,sim);
		List<RecommendedItem> recommendations = recommender.recommend(103
				, 3);
		for (RecommendedItem recommendation : recommendations) {
			coursename = courselist.get(Long.toString(recommendation.getItemID()));
			i=(int)Math.round(recommendation.getValue());
			gradename= gradelist.get(i);
			System.out.println(coursename+" - "+gradename);
		}
		return recommender;	
	}
}

