package salesTransactionsDataset;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//Q.1 Find out number of products sold in each country. 
//Here, it can analyze the product sale of company country wise. It can be found out that in which country company products are more demanding.

public class Q1 {
	//mapper class
		public static class MapForWordCount extends Mapper<LongWritable,Text,Text,IntWritable>
		{
			public void map(LongWritable key,Text value, Context con) throws IOException, InterruptedException
			{
				if (key.get() == 0){
					return;
				}else{
				String line= value.toString();
				String[] words=line.split(",");
//				for(String word:words)
//				{
//					Text outputkey=new Text(word);
//					IntWritable outputvalue=new IntWritable(1);
//					con.write(outputkey,outputvalue);
//				}
				Text outputkey=new Text(words[7]);
				IntWritable outputvalue=new IntWritable(1);
				con.write(outputkey,outputvalue);
			}
			}
		}
		//reducer class
		public static class ReduceForWordCount extends Reducer<Text,IntWritable,Text,IntWritable>
		{
			public void reduce(Text word,Iterable<IntWritable> values,Context con)throws IOException, InterruptedException
			{
				int sum=0;
				for(IntWritable value:values)
				{
					sum=sum+value.get();
				}
				con.write(word,new IntWritable(sum));
			}
			
		}
		public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
			// TODO Auto-generated method stub
			Configuration c= new Configuration();
			Job j=Job.getInstance(c,"Q1");
			j.setJarByClass(Q1.class);
			j.setMapperClass(MapForWordCount.class);
			j.setReducerClass(ReduceForWordCount.class);
			j.setOutputKeyClass(Text.class);
			j.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(j,new Path(args[0]));
			FileOutputFormat.setOutputPath(j,new Path(args[1]));
			System.exit(j.waitForCompletion(true)?0:1);
			
		}
}
