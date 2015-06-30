 <?php




$admin_email = 'kowsikv.92@gmail.com'; // Your Email



class Contact_Form{
	function __construct($details, $admin_email, $message_min_length){
		$this->dat=stripslashes($_POST['rdate']);
		
		$this->name = stripslashes($_POST['rname']);
		$this->email = trim($_POST['remail']);
		$this->mob = trim($_POST['rmob']);

		
		$this->subject = 'Registration from Your Website'; // Subject 
		
	
		$this->admin_email = $admin_email;
	
		$this->data= $this->name."\r\n".$this->email;
		
		
		$this->response_status = 1;
		$this->response_html = '';
	}


	private function validateEmail(){
		$regex = '/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i';
	
		if($this->email == '') { 
			return false;
		} else {
			$string = preg_replace($regex, '', $this->email);
		}
	
		return empty($string) ? true : false;
	}


	private function validateFields(){
		// Check name
		if(!$this->name)
		{
			$this->response_html .= '<p>Please enter your name</p>';
			$this->response_status = 0;
		}
		
		// Check email
		if(!$this->email)
		{
			$this->response_html .= '<p>Please enter an e-mail address</p>';
			$this->response_status = 0;
		}
		
		// Check valid email
		if($this->email && !$this->validateEmail())
		{
			$this->response_html .= '<p>Please enter a valid e-mail address</p>';
			$this->response_status = 0;
		}
		
		// Check message length
		if(!$this->message || strlen($this->message) < $this->message_min_length)
		{
			$this->response_html .= '<p>Please enter your message. It should have at least '.$this->message_min_length.' characters</p>';
			$this->response_status = 0;
		}
	}


	private function sendEmail(){
		$mail = mail($this->admin_email, $this->message, $this->data	);
	
		if($mail)
		{
		echo "1";

		}
		else{
		echo "2";
		}
	}


	function sendRequest(){
		$this->validateFields();
		if($this->response_status)
		{
			$this->sendEmail();
		}

		$response = array();
		$response['status'] = $this->response_status;	
		$response['html'] = $this->response_html;
		
	}
}


$contact_form = new Contact_Form($_POST, $admin_email, $message_min_length);
$contact_form->sendRequest();

?>
                            
                            
                            