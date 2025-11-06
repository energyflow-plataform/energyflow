package com.pi.energyflow.util;

public class EmailTemplates {

	public static String recuperacaoSenha(String nomeUsuario, String link) {
        int ano = java.time.Year.now().getValue();

        return String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                    <table width="100%%" cellpadding="0" cellspacing="0" 
                           style="max-width: 600px; margin: auto; background-color: white; 
                                  border-radius: 10px; overflow: hidden; 
                                  box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                        <tr style="background-color: #10B981;">
                            <td style="padding: 20px; text-align: center; color: white;">
                                <h2>🔐 Recuperação de Senha</h2>
                            </td>
                        </tr>
                        <tr>
                            <td style="padding: 30px;">
                                <p>Olá <strong>%s</strong>,</p>
                                <p>Recebemos uma solicitação para redefinir sua senha no <strong>EnergyFlow</strong>.</p>
                                <p>Clique no botão abaixo para criar uma nova senha. O link é válido por 30 minutos:</p>
                                <p style="text-align: center; margin: 40px 0;">
                                    <a href="%s" 
                                       style="background-color: #10B981; color: white; text-decoration: none; 
                                              padding: 12px 24px; border-radius: 8px; font-weight: bold;">
                                        🔁 Redefinir Senha
                                    </a>
                                </p>
                                <p>Se você não solicitou essa alteração, ignore este e-mail.</p>
                                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                                <p style="font-size: 12px; color: #888;">© %d EnergyFlow. Todos os direitos reservados ⚡</p>
                            </td>
                        </tr>
                    </table>
                </body>
            </html>
        """, nomeUsuario, link, ano);
    }
	
	public static String boasVindas(String nomeUsuario) {
	    int ano = java.time.Year.now().getValue();

	    return String.format("""
	        <html>
	            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
	                <table width="100%%" cellpadding="0" cellspacing="0"
	                       style="max-width: 600px; margin: auto; background-color: white;
	                              border-radius: 10px; overflow: hidden;
	                              box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
	                    <tr style="background-color: #10B981;">
	                        <td style="padding: 20px; text-align: center; color: white;">
	                            <h2>🎉 Bem-vindo ao EnergyFlow!</h2>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td style="padding: 30px;">
	                            <p>Olá <strong>%s</strong>,</p>
	                            <p>Seja muito bem-vindo ao <strong>EnergyFlow</strong> ⚡!</p>
	                            <p>Agora você pode começar a monitorar e otimizar o seu consumo de energia de forma prática e inteligente.</p>
	                            <p style="text-align: center; margin: 40px 0;">
	                                <a href="%s" 
	                                   style="background-color: #10B981; color: white; text-decoration: none; 
	                                          padding: 12px 24px; border-radius: 8px; font-weight: bold;">
	                                    Acessar o EnergyFlow
	                                </a>
	                            </p>
	                            <p>Se precisar de ajuda, estamos à disposição!</p>
	                            <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
	                            <p style="font-size: 12px; color: #888;">© %d EnergyFlow. Todos os direitos reservados ⚡</p>
	                        </td>
	                    </tr>
	                </table>
	            </body>
	        </html>
	    """, nomeUsuario, "https://energyflow-plataform.vercel.app/", ano);
	}
}
