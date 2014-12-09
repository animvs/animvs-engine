package br.com.animvs.engine.net.cliente;

import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.BufferUtils;

final class PacoteRecebimentoBuffer {
	private short pacoteID;
//	private short tamanho;

	private ByteBuffer bufferPacote;

	public final ByteBuffer getBufferPacote() {
		return bufferPacote;
	}

	public final boolean getPacoteCompleto() {
		//4 bytes descontados do cabeçalho
		return ((bufferPacote.capacity() - 4) - bufferPacote.position() == 0);
	}

	public final short getPacoteID() {
		return pacoteID;
	}
	
	public final int getTamanho() {
		return bufferPacote.limit();
	}

	public PacoteRecebimentoBuffer(short tamanhoPacote, short pacoteID) {
		if (tamanhoPacote <= 0)
			throw new RuntimeException("O parâmetro tamanhoPacote deve ser > 0");
		
		if (pacoteID <= 0)
			throw new RuntimeException("O parâmetro pacoteID deve ser > 0");
		
		bufferPacote = BufferUtils.newByteBuffer(tamanhoPacote);
		this.pacoteID = pacoteID;
		

		// bufferPacote.getShort(); // Descarta o primeiro byte (tamanho)
		// pacoteID = bufferPacote.getShort();
	}

	public final void put(byte[] bytes) {
		bufferPacote.put(bytes);
	}

	public final void put(ByteBuffer buffer) {
		bufferPacote.put(buffer);
	}

	public final void put(byte[] bytes, int offset) {
		bufferPacote.put(bytes, offset, bytes.length - offset);
	}
	
	public final void fechaPacote(){
		bufferPacote.compact();
	}
}
