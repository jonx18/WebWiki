package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;


public interface EmailService {
	public void enviar(String fuente, String destino, String asunto, String mensaje);
}
