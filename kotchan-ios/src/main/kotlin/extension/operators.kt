package extension

import kotlinx.cinterop.*
import platform.Foundation.*


operator fun NSDictionary.get(key: NSCopyingProtocol): ObjCObject? {
    return if (this.allKeys.containsObject(key)) this.objectForKey(key) else null
}

operator fun NSMutableDictionary.set(key: NSCopyingProtocol, value: ObjCObject) {
    this.setObject(value, forKey = key)
}

operator fun NSArray.get(idx: Long): ObjCObject {
    return this.objectAtIndex(idx)!!
}