
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import javax.swing.JOptionPane;
// a linha abaixo importa o pacote para preenchimento do endereço através da busca de cep
import br.com.infox.buscacep.WebServiceCep;
// a linha abaixo importa recurso
import net.proteanit.sql.DbUtils;

/**
 *
 * @author MDAVEL
 */
public class TelaCliente extends javax.swing.JInternalFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCiente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }
    
        // Método para adicionar clientes (botão adicionar)
    private void adicionar() {
        String sql = "insert into tbclientes(nomecli,endcli,numcli,complcli,bairrocli,cidadecli,estadocli,celcli,fixocli,emailcli,cep) values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliRua.getText());
            pst.setString(3, txtCliNumero.getText());
            pst.setString(4, txtCliComplemento.getText());
            pst.setString(5, txtCliBairro.getText());
            pst.setString(6, txtCliCidade.getText());
            pst.setString(7, txtCliUf.getText());
            pst.setString(8, txtCliFixo.getText());
            pst.setString(9, txtCliCel.getText());
            pst.setString(10, txtCliMail.getText());
            pst.setString(11, txtCliCep.getText());
            // validação dos campos obrigatórios
            if ((txtCliNome.getText().isEmpty()) || (txtCliFixo.getText().isEmpty()) || (txtCliCel.getText().isEmpty()) || (txtCliCep.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Campo de preenchimento obrigatório está em branco!");

            } else {

                // a linha abaixo atualiza a tabela de usuário com os dados do formulário
                // a linha abaixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve de apoio ao entendimento da lógica
                System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso");
                    // as linhas abaixo limpam os campos para que o usuario possa cadastrar um novo

                    txtCliNome.setText(null);
                    txtCliRua.setText(null);
                    txtCliNumero.setText(null);
                    txtCliComplemento.setText(null);
                    txtCliBairro.setText(null);
                    txtCliCidade.setText(null);
                    txtCliUf.setText(null);
                    txtCliFixo.setText(null);
                    txtCliCel.setText(null);
                    txtCliMail.setText(null);
                    txtCliCep.setText(null);
                    btnExcluir.setEnabled(false);
                    btnEditar.setEnabled(false);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

        // método para pesquisar clientes pelo nome com filtro
    private void pesquisar_cliente() {
        String sql = "select * from tbclientes where nomecli like ?";
        try {
        pst = conexao.prepareStatement(sql);
        
        // passando o conteúdo da caixa de pesquisa para o ?
        // atenção ao % que é a continuação da string sql
        pst.setString(1,txtCliPesquisar.getText() + "%");
        rs=pst.executeQuery();
        
        // a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
        tblCliNome.setModel(DbUtils.resultSetToTableModel(rs));
        System.out.println(rs);
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

        // METODO PARA SETAR O CONTEUDO DA TABELA DE PESQUISA NOS CAMPOS DO FORMULARIO
        public void setar_campos()
    {
        int setar = tblCliNome.getSelectedRow();
        txtCliId.setText(tblCliNome.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblCliNome.getModel().getValueAt(setar, 1).toString());
        txtCliRua.setText(tblCliNome.getModel().getValueAt(setar, 2).toString());
        txtCliNumero.setText(tblCliNome.getModel().getValueAt(setar, 3).toString());
        txtCliComplemento.setText(tblCliNome.getModel().getValueAt(setar, 4).toString());
        txtCliBairro.setText(tblCliNome.getModel().getValueAt(setar, 5).toString());
        txtCliCidade.setText(tblCliNome.getModel().getValueAt(setar, 6).toString());
        txtCliUf.setText(tblCliNome.getModel().getValueAt(setar, 7).toString());
        txtCliFixo.setText(tblCliNome.getModel().getValueAt(setar, 8).toString());
        txtCliCel.setText(tblCliNome.getModel().getValueAt(setar, 9).toString());
        txtCliMail.setText(tblCliNome.getModel().getValueAt(setar, 10).toString());
        txtCliCep.setText(tblCliNome.getModel().getValueAt(setar, 11).toString());
        
        // A LINHA ABAIXO DESABILITA O BOTÃO ADICIONAR PARA QUE O CADASTRO NÃO SEJA DUPLICADO
        // E HABILITA O BOTÃO EXCLUIR
        btnAdicionar.setEnabled(false);
        btnExcluir.setEnabled(true);
        btnEditar.setEnabled(true);
                        
    }

            //Método que realiza a busca de cep e preenche os campos no formulário
        public void buscaCep() {
        //Faz a busca para o cep 58043-280
        WebServiceCep webServiceCep = WebServiceCep.searchCep(txtCliCep.getText());
        //A ferramenta de busca ignora qualquer caracter que n?o seja n?mero.

        //caso a busca ocorra bem, imprime os resultados.
        if (webServiceCep.wasSuccessful()) {
            txtCliRua.setText(webServiceCep.getLogradouroFull());
            txtCliCidade.setText(webServiceCep.getCidade());
            txtCliBairro.setText(webServiceCep.getBairro());
            txtCliUf.setText(webServiceCep.getUf());
            System.out.println("Cep: " + webServiceCep.getCep());
            System.out.println("Logradouro: " + webServiceCep.getLogradouroFull());
            System.out.println("Bairro: " + webServiceCep.getBairro());
            System.out.println("Cidade: "
                    + webServiceCep.getCidade() + "/" + webServiceCep.getUf());

            //caso haja problemas imprime as exce??es.
        } else {
            JOptionPane.showMessageDialog(null, "Erro numero: " + webServiceCep.getResulCode());

            JOptionPane.showMessageDialog(null, "Descrição do erro: " + webServiceCep.getResultText());
        }
    }
    

            // Criando método para alterar dados dos usuários
    
    private void alterar(){
    String sql="update tbclientes set nomecli=?,endcli=?,numcli=?,complcli=?,bairrocli=?,cidadecli=?,estadocli=?,celcli=?,fixocli=?,emailcli=?,cep=? where idcli=?";
    
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1,txtCliNome.getText());
            pst.setString(2,txtCliRua.getText());
            pst.setString(3,txtCliNumero.getText());
            pst.setString(4,txtCliComplemento.getText());
            pst.setString(5,txtCliBairro.getText());
            pst.setString(6,txtCliCidade.getText());
            pst.setString(7,txtCliUf.getText());
            pst.setString(8,txtCliFixo.getText());
            pst.setString(9,txtCliCel.getText());
            pst.setString(10,txtCliMail.getText());
            pst.setString(11,txtCliCep.getText());
            pst.setString(12,txtCliId.getText());
            
            // validação dos campos obrigatórios
            if ((txtCliNome.getText().isEmpty()) || (txtCliCel.getText().isEmpty()) || (txtCliCep.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Campo de preenchimento obrigatório está em branco!");

            } else {

                // a linha abaixo atualiza a tabela de usuário com os dados do formulário
                // a linha abaixo é usada para confirmar a alteração dos dados do usuário na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve de apoio ao entendimento da lógica
                System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso");
                    // as linhas abaixo limpam os campos para que o usuario possa cadastrar um novo
                    txtCliNome.setText(null);
                    txtCliRua.setText(null);
                    txtCliNumero.setText(null);
                    txtCliComplemento.setText(null);
                    txtCliBairro.setText(null);
                    txtCliCidade.setText(null);
                    txtCliUf.setText(null);
                    txtCliFixo.setText(null);
                    txtCliCel.setText(null);
                    txtCliMail.setText(null);
                    txtCliCep.setText(null);
                    
                    btnAdicionar.setEnabled(true);
                    btnEditar.setEnabled(false);
                    btnExcluir.setEnabled(false);
                    
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
     // método responsável pela remoção de cliente
    private void remover(){
    // a estrutura abaixo confirma a remoção de cliente
    int confirma=JOptionPane.showConfirmDialog(null,"Tem certeza de que deseja remover este cliente?","Atenção!",JOptionPane.YES_NO_OPTION);
    if (confirma == JOptionPane.YES_OPTION){
        String sql="delete from tbclientes where idcli=?";
        try {
            pst=conexao.prepareStatement(sql);
            pst.setString(1,txtCliId.getText());
            int apagado=pst.executeUpdate();
            if (apagado > 0){
                JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                    txtCliNome.setText(null);
                    txtCliRua.setText(null);
                    txtCliNumero.setText(null);
                    txtCliComplemento.setText(null);
                    txtCliBairro.setText(null);
                    txtCliCidade.setText(null);
                    txtCliUf.setText(null);
                    txtCliFixo.setText(null);
                    txtCliCel.setText(null);
                    txtCliMail.setText(null);
                    txtCliCep.setText(null);
                    
                    btnAdicionar.setEnabled(true);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        txtCliNome = new javax.swing.JTextField();
        txtCliCep = new javax.swing.JFormattedTextField();
        txtCliRua = new javax.swing.JTextField();
        txtCliNumero = new javax.swing.JTextField();
        txtCliComplemento = new javax.swing.JTextField();
        txtCliBairro = new javax.swing.JTextField();
        txtCliCidade = new javax.swing.JTextField();
        txtCliUf = new javax.swing.JTextField();
        txtCliFixo = new javax.swing.JFormattedTextField();
        txtCliCel = new javax.swing.JFormattedTextField();
        txtCliMail = new javax.swing.JTextField();
        tblClientes = new javax.swing.JScrollPane();
        tblCliNome = new javax.swing.JTable();
        txtCliPesquisar = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        buscaCep = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("SENAI - Cadastro de Clientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/Logo Principal.jpg"))); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Id Cliente:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, -1, -1));

        jLabel2.setText("Nome:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, -1, -1));

        jLabel3.setText("CEP:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, -1, -1));

        jLabel4.setText("Endereço:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, -1, -1));

        jLabel5.setText("Complemento:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, -1, -1));

        jLabel6.setText("nº:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 350, -1, -1));

        jLabel7.setText("Bairro:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, -1, -1));

        jLabel8.setText("Cidade:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 390, -1, -1));

        jLabel9.setText("UF:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 390, -1, -1));

        jLabel10.setText("Telefone Fixo:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, -1, -1));

        jLabel11.setText("Celular:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 430, -1, -1));

        jLabel12.setText("E-mail:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 430, -1, -1));

        txtCliId.setEditable(false);
        txtCliId.setEnabled(false);
        getContentPane().add(txtCliId, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 67, -1));
        getContentPane().add(txtCliNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 385, -1));
        getContentPane().add(txtCliCep, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 125, -1));
        getContentPane().add(txtCliRua, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 385, -1));
        getContentPane().add(txtCliNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 122, -1));
        getContentPane().add(txtCliComplemento, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 204, -1));
        getContentPane().add(txtCliBairro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 390, 211, -1));
        getContentPane().add(txtCliCidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 390, 132, -1));
        getContentPane().add(txtCliUf, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 390, 47, -1));
        getContentPane().add(txtCliFixo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 430, 196, -1));
        getContentPane().add(txtCliCel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 430, 211, -1));
        getContentPane().add(txtCliMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 430, 252, -1));

        tblCliNome.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCliNome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCliNomeMouseClicked(evt);
            }
        });
        tblClientes.setViewportView(tblCliNome);

        getContentPane().add(tblClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, 710, 130));

        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        getContentPane().add(txtCliPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 320, -1));

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdicionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 490, -1, -1));

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        btnExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        getContentPane().add(btnExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, 80, 80));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        btnEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, -1, -1));

        buscaCep.setText("Buscar");
        buscaCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscaCepActionPerformed(evt);
            }
        });
        getContentPane().add(buscaCep, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, -1, 40));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/user.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 120, 160));

        setBounds(0, 0, 1148, 680);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
        
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        alterar();
        
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        remover();
        
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void buscaCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscaCepActionPerformed
        buscaCep();
    }//GEN-LAST:event_buscaCepActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void tblCliNomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliNomeMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblCliNomeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton buscaCep;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTable tblCliNome;
    private javax.swing.JScrollPane tblClientes;
    private javax.swing.JTextField txtCliBairro;
    private javax.swing.JFormattedTextField txtCliCel;
    private javax.swing.JFormattedTextField txtCliCep;
    private javax.swing.JTextField txtCliCidade;
    private javax.swing.JTextField txtCliComplemento;
    private javax.swing.JFormattedTextField txtCliFixo;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliMail;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliNumero;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliRua;
    private javax.swing.JTextField txtCliUf;
    // End of variables declaration//GEN-END:variables
}
