package com.techzone.digi.resource;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.entity.Attachment;
import com.techzone.digi.service.AttachmentService;

@RestController
@RequestMapping(value = "{companyDomain}/attachments/")
public class AttachmentResource {

	@Autowired
	private AttachmentService attachmentService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.PUT, value = "/{attachmentId}")
	public ResponseEntity<Void> update(@RequestParam MultipartFile file, @PathVariable String companyDomain, @PathVariable Long attachmentId)
			throws IllegalStateException, IOException {
		Attachment attachment = attachmentService.save(file, companyDomain, attachmentId);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(attachment.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestParam MultipartFile file, @PathVariable String companyDomain)
			throws IllegalStateException, IOException {
		Attachment attachment = attachmentService.save(file, companyDomain, null);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(attachment.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	/*
	 * @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces =
	 * MediaType.APPLICATION_OCTET_STREAM_VALUE) public ResponseEntity<byte[]>
	 * findById(@PathVariable Long id) throws IllegalStateException, IOException {
	 * File imgPath = new File(attachmentService.uploadDir + "\\" + id.toString());
	 * 
	 * byte[] image = Files.readAllBytes(imgPath.toPath()); HttpHeaders headers =
	 * new HttpHeaders(); headers.setContentType(MediaType.IMAGE_JPEG);
	 * headers.setContentLength(image.length); return new ResponseEntity<>(image,
	 * headers, HttpStatus.OK); }
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> findById(@PathVariable Long id) throws IllegalStateException, IOException {
		byte[] file = attachmentService.loadFileById(id);
		HttpHeaders headers = new HttpHeaders();
		new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentLength(file.length);
		return new ResponseEntity<>(file, headers, HttpStatus.OK);
	}

}
