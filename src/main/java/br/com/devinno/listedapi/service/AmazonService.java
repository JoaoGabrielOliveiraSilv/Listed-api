package br.com.devinno.listedapi.service;

import java.util.Optional;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.model.UserListed;

@Service
public class AmazonService {

	@Value("${AWS_BUCKET_NAME_PROFILE}")
	private String bucketProfile;
	
	@Autowired
	private AmazonS3 s3Client;
	
	public Object uploadImageProfile(Optional<UserListed> user, MultipartFile file) {
		// Check if that the type file is valid
		if(!file.getContentType().equalsIgnoreCase("image/jpeg") && !file.getContentType().equalsIgnoreCase("image/png")) {
			ErrorResponse error = new ErrorResponse();
			error.setTitle("Formato de arquivo inválido");
			error.setMessage("Só é aceito arquivos do formato .jpg");
			error.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
			
			return error;
		} else {
			if(user.get().getImage() != null) {
	        	DeleteObjectRequest requestDelete = new DeleteObjectRequest(bucketProfile, user.get().getImage());
	        	s3Client.deleteObject(requestDelete);
	        }
			
			String fileName = this.generateFilename(user);
			String linkObject = this.generateLink(fileName);
			ObjectMetadata metadata = this.setMetadata(user, file);
			
			try {
				PutObjectRequest objectRequest = new PutObjectRequest(bucketProfile, fileName, file.getInputStream(), metadata)
						// Allow everyone to read images
						.withCannedAcl(CannedAccessControlList.PublicRead);
				
				s3Client.putObject(objectRequest);

				user.get().setImage(linkObject);
				
				return user.get();
			} catch (Exception e) {
				ErrorResponse error = new ErrorResponse();
				error.setTitle("Algo inesperado impediu de fazer o upload da imagem");
				error.setMessage("Tente novamente mais tarde");
				error.setStatus(HttpStatus.EXPECTATION_FAILED);
				
				return error;
			}
		}
	}
	
	private String generateFilename(Optional<UserListed> user) {
		String data = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthOfYear() + "-" + LocalDate.now().getDayOfMonth();
		String time = LocalTime.now().getHourOfDay() + "-" + LocalTime.now().getMinuteOfHour() + "-" + LocalTime.now().getSecondOfMinute(); 
		String fileName = user.get().getId() + "-" + user.get().getUsername().replace("@", "") + "-" + data + "-" + time + ".jpg";
		
		return fileName;
	}
	
	private ObjectMetadata setMetadata(Optional<UserListed> user, MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpeg");
		metadata.addUserMetadata("x-listed-identifier", String.valueOf(user.get().getId()));
		metadata.setContentLength(file.getSize());
		return metadata;
	}
	
	private String generateLink(String fileName) {
		return "https://listed-bucket.s3-sa-east-1.amazonaws.com/image/profile/" + fileName;
	}
}
