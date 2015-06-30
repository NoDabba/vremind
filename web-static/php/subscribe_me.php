                                                                                                                                                                <?php
$admin_email = 'kowsikv.92@gmail.com'; // Your Email
 // Min Message Length



class Contact_Form{
	function __construct($details, $admin_email){
		
		$this->email = trim($details['fmail']);
		$this->subject = 'Subscription from Your Website'; // Subject 
	
	
		$this->admin_email = $admin_email;
		
		$this->data= $this->email;
		
		
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
		
		
	}


	private function sendEmail(){
		$mail = mail($this->admin_email, $this->subject, $this->data);
	
		if($mail)
		{
			echo "1";
         	
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


$contact_form = new Contact_Form($_POST, $admin_email);
$contact_form->sendRequest();
?>
                            
                            
                            
                            
                            
                            