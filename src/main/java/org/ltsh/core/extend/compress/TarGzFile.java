package org.ltsh.core.extend.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import org.ltsh.core.business.exp.BusinessException;
import org.ltsh.core.core.util.FileUtil;

/**
 * 压缩、解压tar.gz文件
 * 依赖org.apache.commons包
 * @author Ych
 * 2019年4月30日
 */
public class TarGzFile implements FileCompress {

	private File tarFile;
	private File plainDir;
	private String charset = "UTF-8";

	private static final int BUFFER_SIZE = 2048;

	public TarGzFile(String tarFile, String plainDir) {
		this.tarFile = new File(tarFile);
		this.plainDir = new File(FileUtil.buildPath(plainDir));
	}
	public TarGzFile(String tarFile) {
		this.tarFile = new File(tarFile);
		this.plainDir = new File(tarFile.substring(0,  tarFile.indexOf(".")));
	}
	public TarGzFile(File tarFile, String plainDir) {
		this.tarFile = tarFile;
		this.plainDir = new File(FileUtil.buildPath(plainDir));
	}

	@Override
	public File compress() throws IOException {
		if (!plainDir.exists()) {
			BusinessException.err("not exist path: " + plainDir.getCanonicalPath());
		}
		FileOutputStream fos = new FileOutputStream(tarFile);
		TarArchiveOutputStream taos = null;
		try {
			taos = new TarArchiveOutputStream(new GzipCompressorOutputStream(fos), charset);
			for (File subFile : plainDir.listFiles()) {
				addEntry(subFile, "", taos);
			}
		} finally {
			FileUtil.close(taos, fos);
		}
		return tarFile;
	}
	
	private void addEntry(File file, String parentPath, TarArchiveOutputStream taos) throws IOException{
		FileInputStream fis = null;
		try {
			if (file.isDirectory()) {
				for (File subFile : file.listFiles()) {
					addEntry(subFile, parentPath + file.getName() + File.separator, taos);
				}
			} else {
				TarArchiveEntry tae = new TarArchiveEntry(parentPath + file.getName());
				tae.setSize(file.length());	//重要
				taos.putArchiveEntry(tae);
				int len;
				byte[] buffer = new byte[BUFFER_SIZE];
				fis = new FileInputStream(file);
				while ((len = fis.read(buffer)) != -1) {
					taos.write(buffer, 0, len);
				}
				taos.closeArchiveEntry();
			}
		} finally {
			FileUtil.close(fis);
		}
	}

	@Override
	public File decompress() throws IOException {
		if (!plainDir.exists()) {
			plainDir.mkdirs();
		}

		FileInputStream fis = null;
		TarArchiveInputStream tarIn = null;
		try {
			TarArchiveEntry entry = null;
			fis = new FileInputStream(tarFile);
			tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(fis), charset);

			while ((entry = tarIn.getNextTarEntry()) != null) {
				if (entry.isDirectory()) {// 是目录
					createDirectory(plainDir.getCanonicalPath(), entry.getName());// 创建空目录
				} else {// 是文件
					File tmpFile = new File(plainDir.getCanonicalPath(), entry.getName());
					createDirectory(tmpFile.getParent(), null);// 创建输出目录
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[BUFFER_SIZE];
						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}
					} catch (IOException e) {
						throw e;
					} finally {
						FileUtil.close(out);
					}
				}
			}
		} finally {
			FileUtil.close(tarIn, fis);
		}

		return null;
	}

	private void createDirectory(String parentPath, String subPath) {
		File file = null;
		if (subPath == null) {
			file = new File(parentPath);
		} else {
			file = new File(parentPath, subPath);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
