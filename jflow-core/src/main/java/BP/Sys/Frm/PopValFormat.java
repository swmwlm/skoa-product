package BP.Sys.Frm;

public enum PopValFormat {
        /// <summary>
        /// 编号
        /// </summary>
        OnlyNo,
        /// <summary>
        /// 名称
        /// </summary>
        OnlyName,
        /// <summary>
        /// 编号与名称
        /// </summary>
        NoName;
        private int intValue;
        
        public int getValue()
    	{
    		return this.ordinal();
    	}
    	
    	public static PopValFormat forValue(int value)
    	{
    		return values()[value];
    	}
}
