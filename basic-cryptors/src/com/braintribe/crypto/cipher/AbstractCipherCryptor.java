// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.crypto.cipher;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Map;

import javax.crypto.Cipher;

import com.braintribe.codec.Codec;
import com.braintribe.crypto.BaseCryptor;
import com.braintribe.crypto.Cryptor;
import com.braintribe.crypto.CryptorException;

/**
 * <p>
 * Provides common functionalities to {@link Cryptor} implementations based on {@link javax.crypto.Cipher}(s).
 * 
 */
public abstract class AbstractCipherCryptor extends BaseCryptor implements CipherCryptor {

	protected String transformation;
	protected String provider;

	AbstractCipherCryptor(String transformation, String provider, Map<Encoding, Codec<byte[], String>> stringCodecs, Charset defaultStringCharset) throws CryptorException {
		super(stringCodecs, null, defaultStringCharset);

		if (transformation == null || transformation.trim().isEmpty()) {
			throw new CryptorException("Cannot build a " + this.getClass().getName() + " without a transformation");
		}

		this.transformation = transformation;
		this.provider = provider;

	}

	public Cipher getCipher() throws CryptorException {

		Cipher cipher = null;

		try {
			if (provider == null) {
				cipher = Cipher.getInstance(transformation);
			} else {
				cipher = Cipher.getInstance(transformation, provider);
			}
		} catch (Exception e) {
			throw CryptorException.wrap("Failed to obtain a Cipher instance for transformation: [" + transformation + "]" + (provider != null ? ", provider: [" + provider + "]" : ""), e);
		}

		return cipher;

	}

	protected Cipher getInitializedCipher(int opmode, Key key) throws CryptorException {

		Cipher cipher = getCipher();

		try {
			cipher.init(opmode, key);
		} catch (Exception e) {
			throw CryptorException.wrap("Failed to initialize a Cipher instance", e);
		}

		return cipher;

	}

}
