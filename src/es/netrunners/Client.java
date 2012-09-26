package es.netrunners;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class Client implements KvmSerializable {

	private int ID;
	private String Name;
	private String Surname;
	private int Age;

	public Client() {
		setID(0);
		setName("");
		setAge(0);
		setSurname("");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return Surname;
	}

	/**
	 * @param surname
	 *            the surname to set
	 */
	public void setSurname(String surname) {
		Surname = surname;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return Age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		Age = age;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return getID();
		case 1:
			return getName();
		case 2:
			return getSurname();
		case 3:
			return getAge();
		default:
			return null;
		}
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void getPropertyInfo(int ind, Hashtable ht, PropertyInfo info) {
		switch (ind) {
		case 0:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "ID";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Name";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "Surname";
			break;
		case 3:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "Age";
			break;
		default:
			break;
		}
	}

	@Override
	public void setProperty(int ind, Object val) {
		switch (ind) {
		case 0:
			setID(Integer.parseInt(val.toString()));
			break;
		case 1:
			setName(val.toString());
			break;
		case 2:
			setSurname(val.toString());
			break;
		case 3:
			setAge(Integer.parseInt(val.toString()));
			break;
		default:
			break;
		}
	}

}
