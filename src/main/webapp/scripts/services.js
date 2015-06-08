'use strict';

/* Services */

jbancApp.factory('LanguageService', function($http, $translate, LANGUAGES) {
	return {
		getBy : function(language) {
			if (language == undefined) {
				language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');
			}
			if (language == undefined) {
				language = 'en';
			}

			var promise = $http.get('i18n/' + language + '.json').then(
					function(response) {
						return LANGUAGES;
					});
			return promise;
		}
	};
});


jbancApp.factory('Sessions', function($resource) {
	return $resource('app/rest/account/sessions/:series', {}, {
		'get' : {
			method : 'GET',
			isArray : true
		}
	});
});

jbancApp.factory('ThreadDumpService', function($http) {
	return {
		dump : function() {
			var promise = $http.get('dump').then(function(response) {
				return response.data;
			});
			return promise;
		}
	};
});

jbancApp.factory('Session', function() {
	this.create = function(login, firstName, lastName, email, userRoles) {
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRoles = userRoles;
	};
	this.invalidate = function() {
		this.login = null;
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.userRoles = null;
	};
	return this;
});

jbancApp.factory('LoginService', function ($resource) {
    return $resource('jbancecho/rest/authservice/login', {}, {
        'login':  { method: 'POST'}
    });
});

jbancApp.factory('LogoutService', function ($resource, StorageService ) {
    return $resource('jbancecho/rest/authservice/logout', {}, {
        'logout':  { method: 'GET', headers: {'authToken': StorageService.get('authToken')}}
    });
});


