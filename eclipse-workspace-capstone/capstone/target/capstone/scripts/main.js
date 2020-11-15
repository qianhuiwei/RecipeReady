(function() {

  /**
   * Variables
   */
  var user_id = '1111';
  var user_fullname = 'John';
	AWS.config.region = 'us-west-2'; // Set your region here
	AWS.config.credentials = new AWS.Credentials('AKIAVYQKYPU6HMH5FOEC', 'GGSIPfRIUAEM3fFQS+43B6K0PVPqN1e8Vn2cqv14'); // Set your credentials here
	const rekognition = new AWS.Rekognition();

  /**
   * Initialize major event handlers
   */
  function init() {
    // register event listeners
    document.querySelector('#login-form-btn').addEventListener('click', onSessionInvalid);
    document.querySelector('#login-btn').addEventListener('click', login);
    document.querySelector('#register-form-btn').addEventListener('click', showRegisterForm);
    document.querySelector('#register-btn').addEventListener('click', register);
    document.querySelector('#inspire-btn').addEventListener('click', loadInspireItems);
	document.querySelector('#fav-btn').addEventListener('click', loadFavoriteItems);
    document.querySelector('#fridge-btn').addEventListener('click', loadFridge);
    document.querySelector('#recommend-btn').addEventListener('click', loadRecommendedItems);
    validateSession();
    // onSessionValid({"user_id":"1111","name":"John Smith","status":"OK"});
  }

  /**
   * Session
   */
  function validateSession() {
    onSessionInvalid();
    // The request parameters
    var url = './login';
    var req = JSON.stringify({});

    // display loading message
    showLoadingMessage('Validating session...');

    // make AJAX call
    ajax('GET', url, req,
      // session is still valid
      function(res) {
        var result = JSON.parse(res);

        if (result.status === 'OK') {
          onSessionValid(result);
        }
      }, function(){
          console.log('login error')
    });
  }

  function onSessionValid(result) {
    user_id = result.user_id;
    user_fullname = result.name;

    var loginForm = document.querySelector('#login-form');
    var registerForm = document.querySelector('#register-form');
    var itemNav = document.querySelector('#item-nav');
    var itemList = document.querySelector('#item-list');
    var avatar = document.querySelector('#avatar');
    var welcomeMsg = document.querySelector('#welcome-msg');
    var logoutBtn = document.querySelector('#logout-link');

    welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

    showElement(itemNav);
    showElement(itemList);
 	loadInspireItems();
    showElement(avatar);
    showElement(welcomeMsg);
    showElement(logoutBtn, 'inline-block');
    hideElement(loginForm);
    hideElement(registerForm);

/*    initGeoLocation();
*/  }

  function onSessionInvalid() {
    var loginForm = document.querySelector('#login-form');
    var registerForm = document.querySelector('#register-form');
    var itemNav = document.querySelector('#item-nav');
    var itemList = document.querySelector('#item-list');
    var avatar = document.querySelector('#avatar');
    var welcomeMsg = document.querySelector('#welcome-msg');
    var logoutBtn = document.querySelector('#logout-link');

    hideElement(itemNav);
    hideElement(itemList);
    hideElement(avatar);
    hideElement(logoutBtn);
    hideElement(welcomeMsg);
    hideElement(registerForm);

    clearLoginError();
    showElement(loginForm);
  }

  function hideElement(element) {
    element.style.display = 'none';
  }

  function showElement(element, style) {
    var displayStyle = style ? style : 'block';
    element.style.display = displayStyle;
  }
  
  function showRegisterForm() {
    var loginForm = document.querySelector('#login-form');
    var registerForm = document.querySelector('#register-form');
    var itemNav = document.querySelector('#item-nav');
    var itemList = document.querySelector('#item-list');
    var avatar = document.querySelector('#avatar');
    var welcomeMsg = document.querySelector('#welcome-msg');
    var logoutBtn = document.querySelector('#logout-link');

    hideElement(itemNav);
    hideElement(itemList);
    hideElement(avatar);
    hideElement(logoutBtn);
    hideElement(welcomeMsg);
    hideElement(loginForm);
    
    clearRegisterResult();
    showElement(registerForm);
  }  

  // -----------------------------------
  // Login
  // -----------------------------------

  function login() {
    var username = document.querySelector('#username').value;
    var password = document.querySelector('#password').value;
    password = md5(username + md5(password));

    // The request parameters
    var url = './login';
    var req = JSON.stringify({
      user_id : username,
      password : password,
    });

    ajax('POST', url, req,
      // successful callback
      function(res) {
        var result = JSON.parse(res);

        // successfully logged in
        if (result.status === 'OK') {
          onSessionValid(result);
        }
      },

      // error
      function() {
        showLoginError();
      });
  }

  function showLoginError() {
    document.querySelector('#login-error').innerHTML = 'Invalid username or password';
  }

  function clearLoginError() {
    document.querySelector('#login-error').innerHTML = '';
  }

  // -----------------------------------
  // Register
  // -----------------------------------

  function register() {
    var username = document.querySelector('#register-username').value;
    var password = document.querySelector('#register-password').value;
    var firstName = document.querySelector('#register-first-name').value;
    var lastName = document.querySelector('#register-last-name').value;
    
	// form validation
    if (username === "" || password == "" || firstName === "" || lastName === "") {
    	showRegisterResult('Please fill in all fields');
    	return
    }
    
    if (username.match(/^[a-z0-9_]+$/) === null) {
    	showRegisterResult('Invalid username');
    	return
    }
    
	// encrypt password
    password = md5(username + md5(password));

    // The request parameters
    var url = './register';
    var req = JSON.stringify({
      user_id : username,
      password : password,
      first_name: firstName,
      last_name: lastName,
    });

    ajax('POST', url, req,
      // successful callback
      function(res) {
        var result = JSON.parse(res);

        // successfully logged in
        if (result.status === 'OK') {
        	showRegisterResult('Succesfully registered');
        } else {
        	showRegisterResult('User already existed');
        }
      },

      // error
      function() {
    	  showRegisterResult('Failed to register');
      });
  }

  function showRegisterResult(registerMessage) {
    document.querySelector('#register-result').innerHTML = registerMessage;
  }

  function clearRegisterResult() {
    document.querySelector('#register-result').innerHTML = '';
  }


  // -----------------------------------
  // Helper Functions
  // -----------------------------------

  /**
   * A helper function that makes a navigation button active
   *
   * @param btnId - The id of the navigation button
   */
  function activeBtn(btnId) {
    var btns = document.querySelectorAll('.main-nav-btn');

    // deactivate all navigation buttons
    for (var i = 0; i < btns.length; i++) {
      btns[i].className = btns[i].className.replace(/\bactive\b/, '');
    }

    // active the one that has id = btnId
    var btn = document.querySelector('#' + btnId);
    btn.className += ' active';
  }

  function showLoadingMessage(msg) {
    var itemList = document.querySelector('#item-list');
    itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' +
      msg + '</p>';
  }

  function showWarningMessage(msg) {
    var itemList = document.querySelector('#item-list');
    itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> ' +
      msg + '</p>';
  }

  function showErrorMessage(msg) {
    var itemList = document.querySelector('#item-list');
    itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> ' +
      msg + '</p>';
  }

  /**
   * A helper function that creates a DOM element <tag options...>
   * @param tag
   * @param options
   * @returns {Element}
   */
  function $create(tag, options) {
    var element = document.createElement(tag);
    for (var key in options) {
      if (options.hasOwnProperty(key)) {
        element[key] = options[key];
      }
    }
    return element;
  }

  /**
   * AJAX helper
   *
   * @param method - GET|POST|PUT|DELETE
   * @param url - API end point
   * @param data - request payload data
   * @param successCallback - Successful callback function
   * @param errorCallback - Error callback function
   */
  function ajax(method, url, data, successCallback, errorCallback) {
    var xhr = new XMLHttpRequest();

    xhr.open(method, url, true);

    xhr.onload = function() {
      if (xhr.status === 200) {
        successCallback(xhr.responseText);
      } else {
        errorCallback();
      }
    };

    xhr.onerror = function() {
      console.error("The request couldn't be completed.");
      errorCallback();
    };

    if (data === null) {
      xhr.send();
    } else {
      xhr.setRequestHeader("Content-Type",
        "application/json;charset=utf-8");
      xhr.send(data);
    }
  }

  // -------------------------------------
  // AJAX call server-side APIs
  // -------------------------------------

  function loadInspireItems() {
	activeBtn('inspire-btn');
	
	// request parameters
    var url = './inspire';
/*    var params = 'user_id=' + user_id;*/
    var req = JSON.stringify({});

    // display loading message
    showLoadingMessage('Loading inspiration items...');

    // make AJAX call
    ajax('GET', url + '?', req, function(res) {
      var items = JSON.parse(res);
      if (!items || items.length === 0) {
        showWarningMessage('No inspiration item.');
      } else {
        listItems(items);
      }
    }, function() {
      showErrorMessage('Cannot load inspiration items.');
    });
}

  /**
   * API #2 Load favorite (or visited) items API end point: [GET]
   * /history?user_id=1111
   */
  function loadFavoriteItems() {
    activeBtn('fav-btn');

    // request parameters
    var url = './history';
    var params = 'user_id=' + user_id;
    var req = JSON.stringify({});

    // display loading message
    showLoadingMessage('Loading favorite items...');

    // make AJAX call
    ajax('GET', url + '?' + params, req, function(res) {
      var items = JSON.parse(res);
      if (!items || items.length === 0) {
        showWarningMessage('No favorite item.');
      } else {
        listItems(items);
      }
    }, function() {
      showErrorMessage('Cannot load favorite items.');
    });
  }

  /**
   * API #3 Load recommended items API end point: [GET]
   * /recommendation?user_id=1111
   */
  function loadRecommendedItems() {
    activeBtn('recommend-btn');

    // request parameters
    var url = './recommend';
    var params = 'user_id=' + user_id;
    var req = JSON.stringify({});

    // display loading message
    showLoadingMessage('Loading recommended items...');

    // make AJAX call
    ajax('GET', url + '?' + params, req, function(res) {
      var items = JSON.parse(res);
      if (!items || items.length === 0) {
        showWarningMessage('No recommeded item.');
      } else {
        listItems(items);
      }
    }, function() {
      showErrorMessage('Cannot load recommeded items.');
    });
  }

function loadFridge() {
    activeBtn('fridge-btn');

    // request parameters
    var url = './fridge';
    var params = 'user_id=' + user_id;
    var req = JSON.stringify({});

    // display loading message
    showLoadingMessage('Loading fridge ingredients...');

    // make AJAX call
    ajax('GET', url + '?' + params, req, function(res) {
      var fridge = JSON.parse(res);
      if (!fridge || fridge.length === 0) {
        showWarningMessage('No ingredients in fridge.');
		listFridge(fridge);
      } else {
        listFridge(fridge);
      }
    }, function() {
      showErrorMessage('Cannot load fridge ingredients.');
    });
  }

  /**
   * API #4 Toggle favorite (or visited) items
   *
   * @param item - The item from the list
   *
   * API end point: [POST]/[DELETE] /history request json data: {
   * user_id: 1111, favorite: item }
   */
  function changeFavoriteItem(item) {
    // check whether this item has been visited or not
    var li = document.querySelector('#item-' + item.item_id);
    var favIcon = document.querySelector('#fav-icon-' + item.item_id);
    var favorite = !(li.dataset.favorite === 'true');

    // request parameters
    var url = './history';
    var req = JSON.stringify({
      user_id: user_id,
      favorite: item
    });
    var method = favorite ? 'POST' : 'DELETE';

    ajax(method, url, req,
      // successful callback
      function(res) {
        var result = JSON.parse(res);
        if (result.status === 'OK' || result.result === 'SUCCESS') {
          li.dataset.favorite = favorite;
          favIcon.className = favorite ? 'fa fa-heart' : 'fa fa-heart-o';
        }
      });
  }

	function addFridgeItem() {
	var fridgeItem = document.getElementById('input-fridge-item').value;
    
	// input validation
    if (fridgeItem === "") {
    	alert('Please input an ingredient');
    	return
    }
    
   /* if (fridgeItem.match(/^[a-z0-9_]+$/) === null) {
    	alert('Invalid ingredient input');
    	return
    }*/

    // The request parameters
    var url = './fridge';
    var req = JSON.stringify({
      user_id : user_id,
      ingredient: fridgeItem,
    });

    ajax('POST', url, req,
      // successful callback
      function(res) {
        var result = JSON.parse(res);

        // successfully add ingredient
        if (result.result === 'SUCCESS') {
			var itemList = document.querySelector('#item-list');
			loadFridge();
/*			loadFridgeItem(itemList, fridgeItem);*/
        }
      },

      // error
      function() {
    	  alert('Failed to add ingredient');
      });
	}

    function deleteFridgeItem(fridgeItem) {
		 var url = './fridge';
   		 var req = JSON.stringify({
      		user_id: user_id,
      		ingredient: fridgeItem
    	});
	ajax('DELETE', url, req,
      // successful callback
      function(res) {
        var result = JSON.parse(res);
        if (result.result === 'SUCCESS') {
			var element = document.getElementById(fridgeItem);
			hideElement(element);
        } 
      }, 
		function() {
    	  alert('Failed to delete ingredient');
      });
	}

  // -------------------------------------
  // Create item list
  // -------------------------------------

  /**
   * List recommendation items base on the data received
   *
   * @param items - An array of item JSON objects
   */
  function listItems(items) {
    var itemList = document.querySelector('#item-list');
    itemList.innerHTML = ''; // clear current results

    for (var i = 0; i < items.length; i++) {
      addItem(itemList, items[i]);
    }
  }

function listFridge(fridge) {
    var itemList = document.querySelector('#item-list');
    itemList.innerHTML = ''; // clear current results

	var section = $create('div');
	section.style.display = 'flex';
	section.style.marginBottom = '16px';

	// add ingredient
    var addText = $create('p');
	addText.innerHTML = 'Add Ingredient';
	section.appendChild(addText);

	var input = $create('input', {
      type: 'text',
	  id: 'input-fridge-item'
    });
	input.style.height = '25px';
	input.style.border = 'none';
	input.style.borderRadius = '4px';
	input.style.marginBottom = '10px';
	input.style.marginLeft = '10px';
	
	var addBtn = $create('button');
	addBtn.textContent='Add';
	addBtn.style.border = 'none';
	addBtn.style.borderRadius = '4px';
	addBtn.style.marginLeft = '10px';
	addBtn.style.paddingLeft = '10px';
	addBtn.style.paddingRight = '10px';
	addBtn.style.background = '#ff8a65';
	
	addBtn.onclick = function() {
   		addFridgeItem();
    };
	section.appendChild(input);
	section.appendChild(addBtn);
	
	// upload picture
	
	var uploadText = $create('p');
	uploadText.innerHTML = 'Upload a picture';
	section.appendChild(uploadText);
	
	var inputImage = $create('input', {
      type: 'file',
	  id: 'real-file'
    });
	
	var uploadBtn = $create('button');
	uploadBtn.textContent='Upload';
	uploadBtn.style.border = 'none';
	uploadBtn.style.borderRadius = '4px';
	uploadBtn.style.marginLeft = '10px';
	uploadBtn.style.paddingLeft = '10px';
	uploadBtn.style.paddingRight = '10px';
	uploadBtn.style.background = '#ff8a65';
	section.appendChild(uploadBtn);
	
	var filenameText = $create('p');
	filenameText.innerHTML = "No file chosen";
	section.appendChild(filenameText);
	
	uploadBtn.addEventListener("click", function() {
		inputImage.click();
	});
	
	var labelText = $create('p');
	
	var image = document.createElement("img");
	
	image.width = "100";
	image.height = "100";
	hideElement(image);
	section.appendChild(image);
	
	inputImage.addEventListener("change", function() {
		if (inputImage.value) {
			// update image file name
			filenameText.innerHTML = inputImage.value.match(/[\/\\]([\w\d\s\.\-\(\)]+)$/)[1];
			
			// process image
			var file = inputImage.files[0];
   			var reader  = new FileReader();
    		reader.onload = function(e)  {
        	/*image.innerHTML = ""; // clear previous image
			image.style.width = "100";
			image.style.height = 'auto';
        	image.src = e.target.result;*/
			detectObjects(e.target.result, labelText);
     		};
			reader.readAsArrayBuffer(file);
     		/*reader.readAsDataURL(file);
			showElement(image);*/
		
   
		} else {
			filenameText.innerHTML = "No file chosen";
		}
	});
	
	// display list of fridge item
	itemList.appendChild(section);
	
	for (var i = 0; i < fridge.length; i++) {
      loadFridgeItem(itemList, fridge[i]);
    }
  }

/**
 * Detect Objects
 * 
 * Uses Rekognition to detect labels and objects
 */
function detectObjects(imgData, labelText) {
	const params = {
		Image: {
			Bytes: imgData
		},
		/*MaxLabels: 5, // (optional) Max number of labels with highest confidence*/
		MinConfidence: 0.55 // (optional) Confidence threshold from 0 to 1, default is 0.55 if left blank
	};
	rekognition.detectLabels(params, function(err, data) {
		if (err) {
			console.log(err, err.stack);
			alert('There was an error detecting the labels in the image provided. Check the console for more details.');
		} else {
			const labels = data.Labels.map((obj) => obj.Name).join(', ');
			alert(labels);
		}
	});
}



function loadFridgeItem(itemList, fridgeItem) {

	var li = $create('li', {
	  id: fridgeItem
    });
	
	li.style.display = 'flex';
	li.style.marginLeft = '20px';
	
	
	li.addEventListener("mouseover", () => {
    	deleteBtn.style.color = '#ff8a65';
	/*	fridgeList.style.color = '#ff8a65';
		fridgeList.style.fontWeight = 'bold';*/
  	});
	
	li.addEventListener("mouseout", () => {
    	deleteBtn.style.color = '#4e342e';
	/*	fridgeList.style.color = '#4e342e';
		fridgeList.style.fontWeight = '300';*/
  	});

	var deleteBtn = $create('p');

    deleteBtn.onclick = function() {
   		deleteFridgeItem(fridgeItem);
    };

    deleteBtn.appendChild($create('i', {
      id: 'checkbox-icon-' + fridgeItem,
      className: 'fa fa-minus-circle'
    }));

    li.appendChild(deleteBtn);

    // ingredients
    var fridgeList = $create('p');

    fridgeList.innerHTML = fridgeItem;
	fridgeList.style.marginLeft = '10px';
    li.appendChild(fridgeList);

    itemList.appendChild(li);
}

  /**
   * Add a single item to the list
   *
   * @param itemList - The <ul id="item-list"> tag (DOM container)
   * @param item - The item data (JSON object)
   *
   <li class="item">
   <img alt="item image" src="https://s3-media3.fl.yelpcdn.com/bphoto/EmBj4qlyQaGd9Q4oXEhEeQ/ms.jpg" />
   <div>
   <a class="item-name" href="#" target="_blank">Item</a>
   <p class="item-keyword">Vegetarian</p>
   </div>
   <p class="item-address">699 Calderon Ave<br/>Mountain View<br/> CA</p>
   <div class="fav-link">
   <i class="fa fa-heart"></i>
   </div>
   </li>
   */
  function addItem(itemList, item) {
    var item_id = item.item_id;

    // create the <li> tag and specify the id and class attributes
   	var li = $create('li', {
      id: 'item-' + item_id,
      className: 'item'
    });

    // set the data attribute ex. <li data-item_id="G5vYZ4kxGQVCR" data-favorite="true">
    li.dataset.item_id = item_id;
    li.dataset.favorite = item.favorite;

    // item image
    if (item.image_url) {
      li.appendChild($create('img', { src: item.image_url }));
    } else {
      li.appendChild($create('img', {
        src: 'https://via.placeholder.com/100'
      }));
    }
    // section
    var section = $create('div');

    // title
    var title = $create('a', {
      className: 'item-name',
      href: item.source_url,
      target: '_blank'
    });
    title.innerHTML = item.title;
    section.appendChild(title);
    li.appendChild(section);

    // keyword
    var keyword = $create('p', {
      className: 'item-keyword'
    });

    keyword.innerHTML = item.ingredients.join(', ');
    section.appendChild(keyword);

    li.appendChild(section);

    // favorite link
    var favLink = $create('p', {
      className: 'fav-link'
    });

    favLink.onclick = function() {
      changeFavoriteItem(item);
    };

    favLink.appendChild($create('i', {
      id: 'fav-icon-' + item_id,
      className: item.favorite ? 'fa fa-heart' : 'fa fa-heart-o'
    }));

	li.appendChild(favLink);
    itemList.appendChild(li);
  }

  init();

})();