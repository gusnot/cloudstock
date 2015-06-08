//'use strict';

/* Controllers */

jbancApp.controller('MainController', function($scope) {
});

jbancApp.controller('LanguageController', function($scope, $translate,
		LanguageService) {
	$scope.changeLanguage = function(languageKey) {
		$translate.use(languageKey);

		LanguageService.getBy(languageKey).then(function(languages) {
			$scope.languages = languages;
		});
	};

	LanguageService.getBy().then(function(languages) {
		$scope.languages = languages;
	});
});

jbancApp.controller('MenuController', function($scope) {
});

jbancApp.controller('LoginController', function($rootScope, $scope, $location, LoginService, StorageService) {
	$scope.authenticationError = false;
	$scope.login = login;
	function login() {
		LoginService.login({
			login : $scope.username,
			password : $scope.password
		},function (response) {
			$rootScope.authenticated = true;
			$location.path('/main').replace();
			StorageService.save('authToken',response.token);
        },function (httpResponse) {
        	$scope.authenticationError = true;
        });
	}
});

jbancApp.controller('LogoutController', function($http, $location, $rootScope, StorageService) {
	$http(
			{
				method : 'GET',
				url : 'jbancecho/rest/authservice/logout',
				headers : {
					'authToken' : StorageService.get('authToken')
				}
	}).success(function(data, status) {
		$rootScope.authenticated = false;
		StorageService.remove('authToken');
		var redirect = $location.path();
		$location.path('/login').search('redirect', redirect).replace();
	}).error(function(data, status) {
		
	});
	
});

jbancApp.controller('ActivationController', function($scope, $routeParams,
		Activate) {
	Activate.get({
		key : $routeParams.key
	}, function(value, responseHeaders) {
		$scope.error = null;
		$scope.success = 'OK';
	}, function(httpResponse) {
		$scope.success = null;
		$scope.error = "ERROR";
	});
});

jbancApp.controller('SessionsController', function($scope, resolvedSessions,
		Sessions) {
	$scope.success = null;
	$scope.error = null;
	$scope.sessions = resolvedSessions;
	$scope.invalidate = function(series) {
		Sessions['delete']({
			series : encodeURIComponent(series)
		}, function(value, responseHeaders) {
			$scope.error = null;
			$scope.success = "OK";
			$scope.sessions = Sessions.get();
		}, function(httpResponse) {
			$scope.success = null;
			$scope.error = "ERROR";
		});
	};
});

jbancApp
		.controller(
				'KeyManagementController',
				function($scope, $http, $location, StorageService) {
					
					$scope.document = {};
					$scope.submit = submit;
					$scope.onChangeFile = onChangeFile;

					function submit() {
						if (validation()) {
							comparePasswordPart($scope.currentSecondPasswordPart, 2);
						}
					}

					function renew() {
						var formData = new FormData();
						formData.append("jbancechoPrivateKey",
								file[0].files[0]);
						formData.append("jbancechoCertificate",
								file[1].files[0]);
						formData.append("thirdPartyCertificate",
								file[2].files[0]);
						formData.append("secondPasswordPart",$scope.secondPasswordPart);
						formData.append("thirdPasswordPart",$scope.thirdPasswordPart);
						$http(
								{
									method : 'POST',
									url : 'jbancecho/rest/adminservice/renewsecurity',
									headers : {
										'Content-Type' : undefined,
										'authToken' : StorageService.get('authToken')
									},
									data : formData,
									transformRequest : function(data,
											headersGetterFunction) {
										return data;
									}
								}).success(function(data, status) {
							$location.path('/success').replace();
						}).error(function(data, status) {
							$scope.renewSecurityError = true;
							$scope.missingFileError = false;
							$scope.missingField = false;
							$scope.jbancEchoPrivateKeyError = false;
							$scope.jbancEchoFileError = false;
							$scope.thirdPartyFileError = false;
							$scope.secondPasswordError = false;
							$scope.thirdPasswordError = false;
							$scope.secondPwdMatchError = false;
							$scope.thirdPwdMatchError = false;
							$scope.weekSecondPwdError = false;
							$scope.weekThirdPwdError = false;
						});
					}

					function comparePasswordPart(passwordPart, passwordPartNumber) {
						var formData = new FormData();
						formData.append("passwordPart", passwordPart);
						formData.append("passwordPartNumber", passwordPartNumber);
						$http(
								{
									method : 'POST',
									url : 'jbancecho/rest/adminservice/comparePassword',
									headers : {
										'Content-Type' : undefined,
										'authToken' : StorageService.get('authToken')
									},
									data : formData,
									transformRequest : function(data,
											headersGetterFunction) {
										return data;
									}
								})
								.success(
										function(data, status) {
											if (passwordPartNumber == 2) {
												comparePasswordPart($scope.currentThirdPasswordPart, 3);
											} else {
												renew();
											}
										}).error(function(data, status) {
											if (passwordPartNumber == 2) {
												$scope.secondPasswordError = true;
												$scope.thirdPasswordError = false;
											} else {
												$scope.secondPasswordError = false;
												$scope.thirdPasswordError = true;
											}
											$scope.renewSecurityError = false;
											$scope.missingFileError = false;
											$scope.missingField = false;
											$scope.jbancEchoPrivateKeyError = false;
											$scope.jbancEchoFileError = false;
											$scope.thirdPartyFileError = false;
											$scope.secondPwdMatchError = false;
											$scope.thirdPwdMatchError = false;
											$scope.weekSecondPwdError = false;
											$scope.weekThirdPwdError = false;
										});
					}

					function onChangeFile(index, value) {
						if (value != null && value != '') {
							$scope.$apply(setAttr(index, value));
							function setAttr() {
								document.getElementsByName('btn-desc')[index].innerHTML = value;
							}
						}
					}

					function validation() {
						var pwdRegExp = /((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,15})/;
						$scope.renewSecurityError = false;
						$scope.missingFileError = false;
						$scope.missingField = false;
						$scope.jbancEchoPrivateKeyError = false;
						$scope.jbancEchoFileError = false;
						$scope.thirdPartyFileError = false;
						$scope.secondPasswordError = false;
						$scope.thirdPasswordError = false;
						$scope.secondPwdMatchError = false;
						$scope.thirdPwdMatchError = false;
						$scope.weekSecondPwdError = false;
						$scope.weekThirdPwdError = false;

						for (var i = 0; i < file.length; i++) {
							if (file[i].files[0] == null) {
								$scope.missingFileError = true;
								return false;
							}
						}
						
						if (!file[0].files[0].name.match(/\.key$/)) {
							$scope.jbancEchoPrivateKeyError = true;
							return false;
						}

						if (!file[1].files[0].name.match(/\.cer$/) && !file[1].files[0].name.match(/\.crt$/)) {
							$scope.jbancEchoFileError = true;
							return false;
						}

						if (!file[2].files[0].name.match(/\.cer$/) && !file[2].files[0].name.match(/\.crt$/)) {
							$scope.thirdPartyFileError = true;
							return false;
						}

//						if ($scope.currentSecondPasswordPart == null) {
//							$scope.missingField = true;
//							return false;
//						}

//						if ($scope.currentThirdPasswordPart == null) {
//							$scope.missingField = true;
//							return false;
//						}

						if ($scope.secondPasswordPart == null) {
							$scope.missingField = true;
							return false;
						}
						
						if (!$scope.secondPasswordPart.match(pwdRegExp)) {
							$scope.weekSecondPwdError = true;
							return false;
						}

						if ($scope.thirdPasswordPart == null) {
							$scope.missingField = true;
							return false;
						}
						
						if (!$scope.thirdPasswordPart.match(pwdRegExp)) {
							$scope.weekThirdPwdError = true;
							return false;
						}

						if ($scope.confirmSecondPasswordPart == null) {
							$scope.missingField = true;
							return false;
						}

						if ($scope.confirmThirdPasswordPart == null) {
							$scope.missingField = true;
							return false;
						}

						if ($scope.secondPasswordPart != $scope.confirmSecondPasswordPart) {
							$scope.secondPwdMatchError = true;
							return false;
						}

						if ($scope.thirdPasswordPart != $scope.confirmThirdPasswordPart) {
							$scope.thirdPwdMatchError = true;
							return false;
						}

						return true;
					}

				});
