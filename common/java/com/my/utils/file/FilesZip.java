package com.my.utils.file;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FilesZip {

	public static boolean compressPic(InputStream inputSream, String descFilePath) {
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;
		// 指定写图片的方式为 jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
		// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
		// 这里指定压缩的程度，参数qality是取值0~1范围内，
		imgWriteParams.setCompressionQuality((float) 0.4);
		imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
		try {
			BufferedImage bufferedImage = ImageIO.read(inputSream);
			FileOutputStream fos = new FileOutputStream(descFilePath);
			imgWrier.reset();
			imgWrier.setOutput(ImageIO.createImageOutputStream(fos));
			// 调用write方法，就可以向输入流写图片
			imgWrier.write(null, new IIOImage(bufferedImage, null, null),imgWriteParams);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
