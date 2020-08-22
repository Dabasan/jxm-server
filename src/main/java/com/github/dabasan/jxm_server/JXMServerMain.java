package com.github.dabasan.jxm_server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py4j.GatewayServer;

/**
 * Main class
 * 
 * @author Daba
 *
 */
public class JXMServerMain {
	private static Logger logger = LoggerFactory.getLogger(JXMServerMain.class);

	private BD1ManipulatorServer bd1Server;

	public static void main(String[] args) {
		var app = new JXMServerMain();
		var server = new GatewayServer(app);
		server.start();

		logger.info("Started to accept connections.");
	}
	public JXMServerMain() {

	}

	public void shutdown() {
		if (bd1Server != null) {
			bd1Server.shutdown();
		}
	}

	// ===== BD1Manipulator =====
	public void startBD1ManipulatorServer(int javaPort, int pythonPort)
			throws UnknownHostException {
		bd1Server = new BD1ManipulatorServer(javaPort, pythonPort);
	}
	public void instantiate_BD1Manipulator() {
		bd1Server.instantiate();
	}
	public void instantiate_BD1Manipulator(String filepath) throws IOException {
		bd1Server.instantiate(filepath);
	}
	public void loadBlocksToLists() {
		bd1Server.loadBlocksToLists();
	}
	public List<Double> getVertexPositionsList_BD1Manipulator() {
		return bd1Server.getVertexPositionsList();
	}
	public List<Double> getUVsList_BD1Manipulator() {
		return bd1Server.getUVsList();
	}
	public List<Integer> getTextureIDsList_BD1Manipulator() {
		return bd1Server.getTextureIDsList();
	}
	public List<Boolean> getEnabledFlagsList_BD1Manipulator() {
		return bd1Server.getEnabledFlagsList();
	}
	public void setBlocksAsLists(List<Double> vertexPositionsList, List<Double> uvsList,
			List<Integer> textureIDsList, List<Boolean> enabledFlagsList) {
		bd1Server.setBlocksAsLists(vertexPositionsList, uvsList, textureIDsList, enabledFlagsList);
	}
	public int getNumBlocks_BD1Manipulator() {
		return bd1Server.getNumBlocks();
	}
	public String getTextureFilename_BD1Manipulator(int textureID) {
		return bd1Server.getTextureFilename(textureID);
	}
	public Map<Integer, String> getTextureFilenames_BD1Manipulator() {
		return bd1Server.getTextureFilenames();
	}
	public void setTextureFilename_BD1Manipulator(int textureID, String textureFilename) {
		bd1Server.setTextureFilename(textureID, textureFilename);
	}
	public void setTextureFilenames_BD1Manipulator(Map<Integer, String> textureFilenames) {
		bd1Server.setTextureFilenames(textureFilenames);
	}
	public void transform_BD1Manipulator(double[] matArray) {
		bd1Server.transform(matArray);
	}
	public void translate_BD1Manipulator(double translationX, double translationY,
			double translationZ) {
		bd1Server.translate(translationX, translationY, translationZ);
	}
	public void rotX_BD1Manipulator(double th) {
		bd1Server.rotX(th);
	}
	public void rotY_BD1Manipulator(double th) {
		bd1Server.rotY(th);
	}
	public void rotZ_BD1Manipulator(double th) {
		bd1Server.rotZ(th);
	}
	public void rot_BD1Manipulator(double axisX, double axisY, double axisZ, double th) {
		bd1Server.rot(axisX, axisY, axisZ, th);
	}
	public void rescale_BD1Manipulator(double scaleX, double scaleY, double scaleZ) {
		bd1Server.rescale(scaleX, scaleY, scaleZ);
	}
	public void invertZ_BD1Manipulator() {
		bd1Server.invertZ();
	}
	public int saveAsBD1_BD1Manipulator(String filepath) {
		return bd1Server.saveAsBD1(filepath);
	}
	public int saveAsOBJ_BD1Manipulator(String filepathObj, String filepathMtl, String mtlFilename,
			boolean flipV) {
		return bd1Server.saveAsOBJ(filepathObj, filepathMtl, mtlFilename, flipV);
	}
}
