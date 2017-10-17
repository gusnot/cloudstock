package com.gusnot.cloudstock.web.rest;

import com.gusnot.cloudstock.CloudstockApp;

import com.gusnot.cloudstock.domain.Transfer;
import com.gusnot.cloudstock.repository.TransferRepository;
import com.gusnot.cloudstock.service.TransferService;
import com.gusnot.cloudstock.repository.search.TransferSearchRepository;
import com.gusnot.cloudstock.service.dto.TransferDTO;
import com.gusnot.cloudstock.service.mapper.TransferMapper;
import com.gusnot.cloudstock.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gusnot.cloudstock.domain.enumeration.TransactionStatus;
/**
 * Test class for the TransferResource REST controller.
 *
 * @see TransferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudstockApp.class)
public class TransferResourceIntTest {

    private static final String DEFAULT_REF_NO = "AAAAAAAAAA";
    private static final String UPDATED_REF_NO = "BBBBBBBBBB";

    private static final TransactionStatus DEFAULT_STATUS = TransactionStatus.ASSIGNED;
    private static final TransactionStatus UPDATED_STATUS = TransactionStatus.APPROVED;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferSearchRepository transferSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransferMockMvc;

    private Transfer transfer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransferResource transferResource = new TransferResource(transferService);
        this.restTransferMockMvc = MockMvcBuilders.standaloneSetup(transferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfer createEntity(EntityManager em) {
        Transfer transfer = new Transfer()
            .refNo(DEFAULT_REF_NO)
            .status(DEFAULT_STATUS);
        return transfer;
    }

    @Before
    public void initTest() {
        transferSearchRepository.deleteAll();
        transfer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransfer() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);
        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate + 1);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getRefNo()).isEqualTo(DEFAULT_REF_NO);
        assertThat(testTransfer.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Transfer in Elasticsearch
        Transfer transferEs = transferSearchRepository.findOne(testTransfer.getId());
        assertThat(transferEs).isEqualToComparingFieldByField(testTransfer);
    }

    @Test
    @Transactional
    public void createTransferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // Create the Transfer with an existing ID
        transfer.setId(1L);
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRefNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setRefNo(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setStatus(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransfers() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get all the transferList
        restTransferMockMvc.perform(get("/api/transfers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].refNo").value(hasItem(DEFAULT_REF_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transfer.getId().intValue()))
            .andExpect(jsonPath("$.refNo").value(DEFAULT_REF_NO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransfer() throws Exception {
        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer
        Transfer updatedTransfer = transferRepository.findOne(transfer.getId());
        updatedTransfer
            .refNo(UPDATED_REF_NO)
            .status(UPDATED_STATUS);
        TransferDTO transferDTO = transferMapper.toDto(updatedTransfer);

        restTransferMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getRefNo()).isEqualTo(UPDATED_REF_NO);
        assertThat(testTransfer.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Transfer in Elasticsearch
        Transfer transferEs = transferSearchRepository.findOne(testTransfer.getId());
        assertThat(transferEs).isEqualToComparingFieldByField(testTransfer);
    }

    @Test
    @Transactional
    public void updateNonExistingTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.toDto(transfer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);
        int databaseSizeBeforeDelete = transferRepository.findAll().size();

        // Get the transfer
        restTransferMockMvc.perform(delete("/api/transfers/{id}", transfer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transferExistsInEs = transferSearchRepository.exists(transfer.getId());
        assertThat(transferExistsInEs).isFalse();

        // Validate the database is empty
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);

        // Search the transfer
        restTransferMockMvc.perform(get("/api/_search/transfers?query=id:" + transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].refNo").value(hasItem(DEFAULT_REF_NO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transfer.class);
        Transfer transfer1 = new Transfer();
        transfer1.setId(1L);
        Transfer transfer2 = new Transfer();
        transfer2.setId(transfer1.getId());
        assertThat(transfer1).isEqualTo(transfer2);
        transfer2.setId(2L);
        assertThat(transfer1).isNotEqualTo(transfer2);
        transfer1.setId(null);
        assertThat(transfer1).isNotEqualTo(transfer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferDTO.class);
        TransferDTO transferDTO1 = new TransferDTO();
        transferDTO1.setId(1L);
        TransferDTO transferDTO2 = new TransferDTO();
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
        transferDTO2.setId(transferDTO1.getId());
        assertThat(transferDTO1).isEqualTo(transferDTO2);
        transferDTO2.setId(2L);
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
        transferDTO1.setId(null);
        assertThat(transferDTO1).isNotEqualTo(transferDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transferMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transferMapper.fromId(null)).isNull();
    }
}
