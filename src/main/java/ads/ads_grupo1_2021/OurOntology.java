package ads.ads_grupo1_2021;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFJsonLDDocumentFormat;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class OurOntology {
	OWLOntologyManager manager;
	OWLOntology ontology;
	
	public OurOntology(InputStream ins) throws OWLOntologyCreationException {
		manager = OWLManager.createOWLOntologyManager();
		ontology =  manager.loadOntologyFromOntologyDocument(ins);
	}
	
	public String createClass(String className, OutputStream outputStream) throws OWLOntologyStorageException, FileNotFoundException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLEntity entity = factory.getOWLEntity(EntityType.CLASS, IRI.create(className));
		OWLAxiom declare = factory.getOWLDeclarationAxiom(entity);
		manager.addAxiom(ontology,declare);
		manager.saveOntology(ontology, outputStream);
		return className;
	}
	
	public String createIndividualOfClass(String individualName, String className, OutputStream outputStream) throws OWLOntologyStorageException {
		OWLDataFactory factory = OWLManager.getOWLDataFactory();
		OWLEntity entity = factory.getOWLEntity(EntityType.NAMED_INDIVIDUAL, IRI.create(className));
		OWLAxiom declare = factory.getOWLDeclarationAxiom(entity);
		manager.addAxiom(ontology,declare);
		manager.saveOntology(ontology, outputStream);
		return individualName;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology o = m.createOntology();
			File file = new File("testes.owl");
			m.saveOntology(o, IRI.create(file));
			System.out.println("hello");
		} catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream input = new FileInputStream(new File("testes.owl"));
		OurOntology ourOntology = new OurOntology(input);
		OutputStream output = new FileOutputStream(new File("testes.owl"));
		//ourOntology.createClass("hello", output);
		System.out.println("done");
	}
}
