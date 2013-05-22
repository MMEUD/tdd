/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.iolma.studio.process.capture.openimaj;

import org.bridj.Pointer;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPRuntime;

/**
 * <i>native declaration : line 1</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.free.fr/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("OpenIMAJGrabber")
@Runtime(CPPRuntime.class)
@SuppressWarnings("all")
public final class Device extends CPPObject {
	public Device() {
		super();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Device(Pointer pointer) {
		super(pointer);
	}
	
	/// C type : const char*
	@Field(0) 
	protected Pointer<Byte > name() {
		return this.io.getPointerField(this, 0);
	}
	/// C type : const char*
	@Field(0) 
	protected Device name(Pointer<Byte > name) {
		this.io.setPointerField(this, 0, name);
		return this;
	}
	/// C type : const char*
	@Field(1) 
	protected Pointer<Byte > identifier() {
		return this.io.getPointerField(this, 1);
	}
	/// C type : const char*
	@Field(1) 
	protected Device identifier(Pointer<Byte > identifier) {
		this.io.setPointerField(this, 1, identifier);
		return this;
	}
	
	protected native Pointer<Byte> getName();
	
	public String getNameStr() {
		return getName().getCString();
	}
	
	protected native Pointer<Byte> getIdentifier();
	
	public String getIdentifierStr() {
		return getIdentifier().getCString();
	}
	
	/* (non-Javadoc)
	 * @see org.bridj.NativeObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o.toString().equals(this.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public String toString() {
		return String.format("Device[%s]=\"%s\"", getIdentifierStr(), getNameStr());
	}
}
