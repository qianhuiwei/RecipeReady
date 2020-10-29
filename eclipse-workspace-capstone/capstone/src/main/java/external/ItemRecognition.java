package external;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;



public class ItemRecognition {
	public static void main(String[] args) throws Exception {
//	public static List<String> getLables(String imageURL) throws FileNotFoundException, IOException {
		String imageURL = "https://www.google.com/search?q=groceries+images&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiF6Om259rsAhXNGTQIHWYhBikQ_AUoAXoECA0QAw&biw=1627&bih=945#imgrc=XEjlZIH9muWjNM";
		List <String> output = new ArrayList();
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
        // Change photo to the path and filename of your image.
    	String photo="groceries.jpg";

    	// read the image file
        ByteBuffer imageBytes;
//        try (InputStream inputStream = new FileInputStream(new File(photo))) {
//            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
//        }
        try (InputStream inputStream = new URL(imageURL).openStream()) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }

        // create an AmazonRekognition client to request AWS service
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(10)
                .withMinConfidence(77F);

        // send the request to AWS server and get labels 
        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List <Label> labels = result.getLabels();

            System.out.println("Detected labels for " + photo);
            for (Label label: labels) {
            	System.out.println(label.getName());
            	output.add(label.getName());
//               System.out.println(label.getName() + ": " + label.getConfidence().toString());
            }

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        }
        
//        return output;
	}
				
		
//    }
}