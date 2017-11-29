package com.progresssoft.deal.boundary.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.Context;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.progresssoft.deal.control.reader.IReader;
import com.progresssoft.deal.control.reader.ReadNIOFileImpl;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.resource.BaseTest;

//@org.junit.runner.RunWith(org.jboss.arquillian.junit.Arquillian.class)
public class TaskServiceTest extends BaseTest {

	private static Context ctx;
	private static EJBContainer ejbContainer;

	@EJB
	private TaskServiceBean service;

	@Deployment
	public static WebArchive createDeployment() {
		// return ShrinkWrap.create(JavaArchive.class, "test.jar")
		return ShrinkWrap.create(WebArchive.class)
				.addPackage(IReader.class.getPackage())
				.addClasses(TaskServiceBean.class)
				.setWebXML(new FileAsset(new File("../progresssoft-assesment-web/src/main/webapp/WEB-INF/web.xml")))
				.addAsResource("META-INF/persistence.xml")
				.addAsManifestResource("logback-test.xml", ArchivePaths.create("logback.xml"))
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); 
	}

	@Before
	public void setUp() {
		super.setUp();
		service = new TaskServiceBean();
		service.emf = getEmfMySQL();
		service.executorService = mock(ManagedExecutorService.class);
		//service.executorService = Executors.newFixedThreadPool(10);
		
		service.reader = new ReadNIOFileImpl();
	}

	@Test
	public void executorTask_Test() {
		DataFileInDTO dataFileInDTO = new DataFileInDTO();
		dataFileInDTO.setFileDTO(getFileCleanDTO());

		DataFileOutDTO dataFileOutDTO = null;
		try {
			dataFileOutDTO = service.executor(dataFileInDTO);
		} catch (DealServiceException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		LOG.info("\n" + dataFileOutDTO.getInfoLoad());
		assertEquals(5, dataFileOutDTO.getCountLine());
	}
	
	public void setUpContainer() {
		ejbContainer = EJBContainer.createEJBContainer();
		System.out.println("Opening the container");
		ctx = ejbContainer.getContext();
		try {
			ctx.lookup("java:global/progresssoft-assesment-ejb/ItemService");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDwon() {
		ejbContainer.close();
	}

}
