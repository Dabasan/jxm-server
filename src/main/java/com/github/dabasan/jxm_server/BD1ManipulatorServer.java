package com.github.dabasan.jxm_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dabasan.ejml_3dtools.Matrix;
import com.github.dabasan.ejml_3dtools.Vector;
import com.github.dabasan.jxm.bd1.BD1Block;
import com.github.dabasan.jxm.bd1.BD1Manipulator;
import com.github.dabasan.jxm.bd1.UV;

import py4j.GatewayServer;

/**
 * Server for BD1Manipulator
 * 
 * @author Daba
 *
 */
class BD1ManipulatorServer implements IServer {
	private Logger logger = LoggerFactory.getLogger(BD1ManipulatorServer.class);

	private GatewayServer server;

	private BD1Manipulator manipulator;
	private List<Double> vertexPositionsList;
	private List<Double> uvsList;
	private List<Integer> textureIDsList;
	private List<Boolean> enabledFlagsList;

	public BD1ManipulatorServer(int javaPort, int pythonPort) throws UnknownHostException {
		server = new GatewayServer.GatewayServerBuilder().javaPort(javaPort)
				.javaAddress(InetAddress.getByName("127.0.0.1"))
				.callbackClient(pythonPort, InetAddress.getByName("127.0.0.1")).entryPoint(this)
				.build();
		server.start();

		logger.info("BD1ManipulatorServer was started.");

		vertexPositionsList = new ArrayList<>();
		uvsList = new ArrayList<>();
		textureIDsList = new ArrayList<>();
		enabledFlagsList = new ArrayList<>();
	}

	@Override
	public void instantiate() {
		manipulator = new BD1Manipulator();
		logger.info("BD1Manipulator was instantiated.");
	}
	@Override
	public void instantiate(String filepath) throws IOException {
		manipulator = new BD1Manipulator(filepath);
		logger.info("BD1Manipulator was instantiated with the following filepath. filepath={}",
				filepath);
	}

	@Override
	public void shutdown() {
		server.shutdown();
		logger.info("BD1ManipulatorServer was shutdown.");
	}

	public void loadBlocksToLists() {
		vertexPositionsList.clear();
		uvsList.clear();
		textureIDsList.clear();
		enabledFlagsList.clear();

		List<BD1Block> blocks = manipulator.getBlocks();
		for (var block : blocks) {
			Vector[] vertexPositions = block.getVertexPositions();
			UV[] uvs = block.getUVs();
			int[] textureIDs = block.getTextureIDs();
			boolean enabled = block.isEnabled();

			for (var vertexPosition : vertexPositions) {
				vertexPositionsList.add(vertexPosition.getX());
				vertexPositionsList.add(vertexPosition.getY());
				vertexPositionsList.add(vertexPosition.getZ());
			}
			for (var uv : uvs) {
				uvsList.add(uv.getU());
				uvsList.add(uv.getV());
			}
			for (int textureID : textureIDs) {
				textureIDsList.add(textureID);
			}
			enabledFlagsList.add(enabled);
		}
	}
	public List<Double> getVertexPositionsList() {
		return vertexPositionsList;
	}
	public List<Double> getUVsList() {
		return uvsList;
	}
	public List<Integer> getTextureIDsList() {
		return textureIDsList;
	}
	public List<Boolean> getEnabledFlagsList() {
		return enabledFlagsList;
	}
	public void setBlocksAsLists(List<Double> vertexPositionsList, List<Double> uvsList,
			List<Integer> textureIDsList, List<Boolean> enabledFlagsList) {
		int numBlocks = enabledFlagsList.size();

		var blocks = new ArrayList<BD1Block>();
		for (int i = 0; i < numBlocks; i++) {
			// Vertex positions
			var vertexPositions = new Vector[8];
			for (int j = 0; j < 8; j++) {
				int index = i * 24 + j * 3;
				double x = vertexPositionsList.get(index);
				double y = vertexPositionsList.get(index + 1);
				double z = vertexPositionsList.get(index + 2);

				vertexPositions[j] = new Vector(x, y, z);
			}

			// UVs
			var uvs = new UV[24];
			for (int j = 0; j < 24; j++) {
				int index = i * 48 + j * 2;
				double u = uvsList.get(index);
				double v = uvsList.get(index + 1);

				uvs[j] = new UV(u, v);
			}

			// Texture IDs
			int[] textureIDs = new int[6];
			for (int j = 0; j < 6; j++) {
				int index = i * 6 + j;
				int textureID = textureIDsList.get(index);

				textureIDs[j] = textureID;
			}

			// Enabled
			boolean enabled = enabledFlagsList.get(i);

			var block = new BD1Block();
			block.setVertexPositions(vertexPositions);
			block.setUVs(uvs);
			block.setTextureIDs(textureIDs);
			block.setEnabled(enabled);

			blocks.add(block);
		}

		manipulator.setBlocks(blocks);
	}

	public int getNumBlocks() {
		return manipulator.getNumBlocks();
	}

	public String getTextureFilename(int textureID) {
		return manipulator.getTextureFilename(textureID);
	}
	public Map<Integer, String> getTextureFilenames() {
		return manipulator.getTextureFilenames();
	}

	public void setTextureFilename(int textureID, String textureFilename) {
		manipulator.setTextureFilename(textureID, textureFilename);
	}
	public void setTextureFilenames(Map<Integer, String> textureFilenames) {
		manipulator.setTextureFilenames(textureFilenames);
	}

	public void transform(double[] matArray) {
		var mat = new Matrix();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				mat.set(i, j, matArray[i * 4 + j]);
			}
		}

		manipulator.transform(mat);
	}

	public void translate(double translationX, double translationY, double translationZ) {
		manipulator.translate(translationX, translationY, translationZ);
	}

	public void rotX(double th) {
		manipulator.rotX(th);
	}
	public void rotY(double th) {
		manipulator.rotY(th);
	}
	public void rotZ(double th) {
		manipulator.rotZ(th);
	}
	public void rot(double axisX, double axisY, double axisZ, double th) {
		manipulator.rot(axisX, axisY, axisZ, th);
	}

	public void rescale(double scaleX, double scaleY, double scaleZ) {
		manipulator.rescale(scaleX, scaleY, scaleZ);
	}

	public void invertZ() {
		manipulator.invertZ();
	}

	public int saveAsBD1(String filepath) {
		return manipulator.saveAsBD1(filepath);
	}
	public int saveAsOBJ(String filepathObj, String filepathMtl, String mtlFilename,
			boolean flipV) {
		return manipulator.saveAsOBJ(filepathObj, filepathMtl, mtlFilename, flipV);
	}
}
