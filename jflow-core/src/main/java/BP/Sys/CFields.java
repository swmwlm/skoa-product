package BP.Sys;

import BP.En.Entities;
import BP.En.Entity;

/**
 * 列选择s
 */
public class CFields extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 列选择s
	 */
	public CFields()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new CField();
	}
}