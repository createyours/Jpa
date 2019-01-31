//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <pre>
 * データ変換クラス
 * </pre>
 */
public class DataConvertUtils {

	/**
	 * オブジェクトをマップに変換する
	 *
	 * @param obj         変換オブジェクト
	 * @param ignoreItems 対象外項目
	 * 
	 * @return 変換後マップ
	 */
	public static Map<String, Object> convertObjectToMap(Object obj, String... ignoreItems)
			throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (null != ignoreItems && Arrays.asList(ignoreItems).contains(field.getName())) {
				continue;
			}
			field.setAccessible(true);
			String key = field.getName();
			// アノテーション（Jsonproperty）がある場合、アノテーション名前を設定する
			if (null != field.getAnnotation(JsonProperty.class)) {
				key = field.getAnnotation(JsonProperty.class).value();
			}
			map.put(key, field.get(obj));
		}
		return map;
	}

}
