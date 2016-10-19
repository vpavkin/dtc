package dtc

package object js {

  private[js] def dayOfWeekJVMToJS(jvmDay: Int) = if (jvmDay == 7) 0 else jvmDay
  private[js] def dayOfWeekJSToJVM(momentDay: Int) = if (momentDay == 0) 7 else momentDay
}
