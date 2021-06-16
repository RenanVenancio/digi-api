package com.techzone.digi.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techzone.digi.entity.Attachment;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.User;
import com.techzone.digi.repository.AttachmentRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private CompanyService companyService;

	@Value("${attachment.directory.root}")
	public String uploadDir;

	public Attachment save(MultipartFile file, String companyDomain, Long attachmentId) {
		Company company = companyService.findByDomain(companyDomain);
		checkPermissions(company.getId());
		Attachment attachment = new Attachment();
		if(attachmentId != null) {
			attachment = attachmentRepository.findById(attachmentId).orElseThrow(() -> new ObjectNotFoundException(Attachment.class.getName() + " not found"));
		}
		String fileName = file.getOriginalFilename();
		attachment.setName(file.getOriginalFilename());
		attachment.setType(fileName.substring(fileName.lastIndexOf(".") + 1));
		attachment.setIsPublic(true);
		attachment.setCompany(company);
		attachment = attachmentRepository.save(attachment);

		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
			try {
				img = simpleResizeImage(img, 600);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File f = new File(uploadDir);
			ImageIO.write(img, attachment.getType(), new File(f, attachment.getId().toString()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return attachment;
	}
	
	BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {
	    return Scalr.resize(originalImage, targetWidth);
	}

	private void checkPermissions(Long companyId) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != companyId)
			throw new AuthorizationException("Acesso negado.");
	}

	public Attachment findById(Long id) {
		Optional<Attachment> attachment = attachmentRepository.findById(id);
		return attachment.orElseThrow(() -> new ObjectNotFoundException(Attachment.class.getName() + " not found"));
	}

	public byte[] loadFileById(Long id) {
		Attachment attachment = findById(id);
		if (!attachment.getIsPublic()) {
			throw new AuthorizationException("Acesso negado.");
		}
		File imgPath = new File(uploadDir + "\\" + id.toString());
		byte[] image;
		try {
			image = Files.readAllBytes(imgPath.toPath());
			return image;
		} catch (IOException e) {
			throw new RuntimeException("Erro ao localizar o arquivo");
		}
	}


}
