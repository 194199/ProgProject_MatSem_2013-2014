package bankserv;

public class PackageInfo {
	/**
	 * Informacja :: numer konta
	 * Format     :: XXXXXXXXXXXXXXXXXXXXXXXXXX
	 */
	public static final int INFO_NUMBER_ACCOUNT = 0x00;
	
	/**
	 * Informacja :: nieistniejący numer konta
	 * Format     :: <empty>
	 */
	public static final int ACCOUNT_NOT_EXISTS = 0x01;
	
        /**
	 * Informacja :: dostęp zdalny do konta zablokowany
	 * Format     :: <empty>
	 */
	public static final int ACCOUNT_BLOCKED = 0xF1;
        
	/**
	 * Informacja :: rządanie wysłania sumy kontrolnej numeru android_id oraz imei
	 * Format     :: <empty>
	 */
	public static final int ID_IMEI_REQUEST_ENC = 0x02;

	/**
	 * Informacja :: rządanie wysłania jawnej postaci numeru android_id oraz imei
	 * Format     ::<empty>
	 */
	public static final int ID_IMEI_REQUEST = 0x03;

	/**
	 * Informacja :: suma kontrolna android_id i imei
	 * Format     :: ENC(<android_id>##<imei>)
	 */
	public static final int INFO_ID_IMEI_ENC = 0x04;

	/**
	 * Informacja :: android_id oraz imei
	 * Format     :: <android_id>##<imei>
	 */
	public static final int INFO_ID_IMEI = 0x05;
	
	/**
	 * Informacja :: rządanie wysłania sumy kontrolnej sekretnego kodu
	 * Format     :: <empty>
	 */
	public static final int SECRED_CODE_REQUEST = 0x06;

	/**
	 * Informacja :: Szczególowe informacje o koncie - akceptacja połaczenia
	 * Format     :: <imie>##<nazwisko>##<balance>
	 */
	public static final int INFO_ACCOUNT_DETAILS = 0x07;

	/**
	 * Informacja :: suma kontrolna sekretnego kodu
	 * Format     :: ENC(<secred code>)
	 */
	public static final int INFO_SECRED_CODE = 0x08;

	/**
	 * Informacja :: odrzucenie sekretnego kodu - błędny kod
	 * Format     :: <#wrongs>
	 */
	public static final int SECRED_CODE_DENIED = 0x09;
}
