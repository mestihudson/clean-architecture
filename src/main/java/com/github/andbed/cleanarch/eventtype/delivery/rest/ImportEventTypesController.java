package com.github.andbed.cleanarch.eventtype.delivery.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.andbed.cleanarch.eventtype.core.boundary.MessageCode;
import com.github.andbed.cleanarch.eventtype.core.boundary.provide.Command;
import com.github.andbed.cleanarch.eventtype.core.boundary.provide.EventTypeRequestModel;
import com.github.andbed.cleanarch.eventtype.core.boundary.provide.ImportPresenter;
import com.github.andbed.cleanarch.eventtype.infrastructure.di.SpringDIFactory;

@Controller
@RequestMapping(ImportEventTypesController.URL)
public class ImportEventTypesController {

	public static final String URL = "/eventimport";
	private final SpringDIFactory factory;

	public ImportEventTypesController(SpringDIFactory factory) {
		this.factory = factory;
	}

	@RequestMapping(method = RequestMethod.GET)
	public void importEventTypes(EventTypeRequestModel params) {
		Presenter presenter = new Presenter();

		Command importEventTypes = factory.
				createImportEventTypesCommand(presenter, params);

		importEventTypes.execute();
	}

	class Presenter implements ImportPresenter {

		private Boolean result;
		private MessageCode code;

		@Override
		public void sendMessage(MessageCode messageCode) {
			this.code = messageCode;
		}

		public ResponseEntity<Boolean> generateResponse() {
			return code == null ?
					new ResponseEntity<>(result, HttpStatus.OK) :
					new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		@Override
		public void sendResult(boolean result) {
			this.result = result;
		}

	};
}
