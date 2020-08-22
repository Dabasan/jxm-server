package com.github.dabasan.jxm_server;

import java.io.IOException;

/**
 * Interface for servers
 * 
 * @author Daba
 *
 */
interface IServer {
	void instantiate();
	void instantiate(String filepath) throws IOException;

	void shutdown();
}
